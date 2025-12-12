import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWarehouseLocation, NewWarehouseLocation } from '../warehouse-location.model';

export type PartialUpdateWarehouseLocation = Partial<IWarehouseLocation> & Pick<IWarehouseLocation, 'id'>;

export type EntityResponseType = HttpResponse<IWarehouseLocation>;
export type EntityArrayResponseType = HttpResponse<IWarehouseLocation[]>;

@Injectable({ providedIn: 'root' })
export class WarehouseLocationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/warehouse-locations');

  create(warehouseLocation: NewWarehouseLocation): Observable<EntityResponseType> {
    return this.http.post<IWarehouseLocation>(this.resourceUrl, warehouseLocation, { observe: 'response' });
  }

  update(warehouseLocation: IWarehouseLocation): Observable<EntityResponseType> {
    return this.http.put<IWarehouseLocation>(
      `${this.resourceUrl}/${this.getWarehouseLocationIdentifier(warehouseLocation)}`,
      warehouseLocation,
      { observe: 'response' },
    );
  }

  partialUpdate(warehouseLocation: PartialUpdateWarehouseLocation): Observable<EntityResponseType> {
    return this.http.patch<IWarehouseLocation>(
      `${this.resourceUrl}/${this.getWarehouseLocationIdentifier(warehouseLocation)}`,
      warehouseLocation,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWarehouseLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWarehouseLocation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWarehouseLocationIdentifier(warehouseLocation: Pick<IWarehouseLocation, 'id'>): number {
    return warehouseLocation.id;
  }

  compareWarehouseLocation(o1: Pick<IWarehouseLocation, 'id'> | null, o2: Pick<IWarehouseLocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getWarehouseLocationIdentifier(o1) === this.getWarehouseLocationIdentifier(o2) : o1 === o2;
  }

  addWarehouseLocationToCollectionIfMissing<Type extends Pick<IWarehouseLocation, 'id'>>(
    warehouseLocationCollection: Type[],
    ...warehouseLocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const warehouseLocations: Type[] = warehouseLocationsToCheck.filter(isPresent);
    if (warehouseLocations.length > 0) {
      const warehouseLocationCollectionIdentifiers = warehouseLocationCollection.map(warehouseLocationItem =>
        this.getWarehouseLocationIdentifier(warehouseLocationItem),
      );
      const warehouseLocationsToAdd = warehouseLocations.filter(warehouseLocationItem => {
        const warehouseLocationIdentifier = this.getWarehouseLocationIdentifier(warehouseLocationItem);
        if (warehouseLocationCollectionIdentifiers.includes(warehouseLocationIdentifier)) {
          return false;
        }
        warehouseLocationCollectionIdentifiers.push(warehouseLocationIdentifier);
        return true;
      });
      return [...warehouseLocationsToAdd, ...warehouseLocationCollection];
    }
    return warehouseLocationCollection;
  }
}
