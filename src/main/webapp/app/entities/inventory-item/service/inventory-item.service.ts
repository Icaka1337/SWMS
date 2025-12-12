import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInventoryItem, NewInventoryItem } from '../inventory-item.model';

export type PartialUpdateInventoryItem = Partial<IInventoryItem> & Pick<IInventoryItem, 'id'>;

type RestOf<T extends IInventoryItem | NewInventoryItem> = Omit<T, 'lastUpdated'> & {
  lastUpdated?: string | null;
};

export type RestInventoryItem = RestOf<IInventoryItem>;

export type NewRestInventoryItem = RestOf<NewInventoryItem>;

export type PartialUpdateRestInventoryItem = RestOf<PartialUpdateInventoryItem>;

export type EntityResponseType = HttpResponse<IInventoryItem>;
export type EntityArrayResponseType = HttpResponse<IInventoryItem[]>;

@Injectable({ providedIn: 'root' })
export class InventoryItemService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inventory-items');

  create(inventoryItem: NewInventoryItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventoryItem);
    return this.http
      .post<RestInventoryItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inventoryItem: IInventoryItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventoryItem);
    return this.http
      .put<RestInventoryItem>(`${this.resourceUrl}/${this.getInventoryItemIdentifier(inventoryItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inventoryItem: PartialUpdateInventoryItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventoryItem);
    return this.http
      .patch<RestInventoryItem>(`${this.resourceUrl}/${this.getInventoryItemIdentifier(inventoryItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInventoryItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInventoryItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInventoryItemIdentifier(inventoryItem: Pick<IInventoryItem, 'id'>): number {
    return inventoryItem.id;
  }

  compareInventoryItem(o1: Pick<IInventoryItem, 'id'> | null, o2: Pick<IInventoryItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getInventoryItemIdentifier(o1) === this.getInventoryItemIdentifier(o2) : o1 === o2;
  }

  addInventoryItemToCollectionIfMissing<Type extends Pick<IInventoryItem, 'id'>>(
    inventoryItemCollection: Type[],
    ...inventoryItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inventoryItems: Type[] = inventoryItemsToCheck.filter(isPresent);
    if (inventoryItems.length > 0) {
      const inventoryItemCollectionIdentifiers = inventoryItemCollection.map(inventoryItemItem =>
        this.getInventoryItemIdentifier(inventoryItemItem),
      );
      const inventoryItemsToAdd = inventoryItems.filter(inventoryItemItem => {
        const inventoryItemIdentifier = this.getInventoryItemIdentifier(inventoryItemItem);
        if (inventoryItemCollectionIdentifiers.includes(inventoryItemIdentifier)) {
          return false;
        }
        inventoryItemCollectionIdentifiers.push(inventoryItemIdentifier);
        return true;
      });
      return [...inventoryItemsToAdd, ...inventoryItemCollection];
    }
    return inventoryItemCollection;
  }

  protected convertDateFromClient<T extends IInventoryItem | NewInventoryItem | PartialUpdateInventoryItem>(inventoryItem: T): RestOf<T> {
    return {
      ...inventoryItem,
      lastUpdated: inventoryItem.lastUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restInventoryItem: RestInventoryItem): IInventoryItem {
    return {
      ...restInventoryItem,
      lastUpdated: restInventoryItem.lastUpdated ? dayjs(restInventoryItem.lastUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInventoryItem>): HttpResponse<IInventoryItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInventoryItem[]>): HttpResponse<IInventoryItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
