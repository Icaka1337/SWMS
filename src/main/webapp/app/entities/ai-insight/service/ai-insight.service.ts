import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAIInsight, NewAIInsight } from '../ai-insight.model';

export type PartialUpdateAIInsight = Partial<IAIInsight> & Pick<IAIInsight, 'id'>;

type RestOf<T extends IAIInsight | NewAIInsight> = Omit<T, 'generatedAt'> & {
  generatedAt?: string | null;
};

export type RestAIInsight = RestOf<IAIInsight>;

export type NewRestAIInsight = RestOf<NewAIInsight>;

export type PartialUpdateRestAIInsight = RestOf<PartialUpdateAIInsight>;

export type EntityResponseType = HttpResponse<IAIInsight>;
export type EntityArrayResponseType = HttpResponse<IAIInsight[]>;

@Injectable({ providedIn: 'root' })
export class AIInsightService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ai-insights');

  create(aIInsight: NewAIInsight): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aIInsight);
    return this.http
      .post<RestAIInsight>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aIInsight: IAIInsight): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aIInsight);
    return this.http
      .put<RestAIInsight>(`${this.resourceUrl}/${this.getAIInsightIdentifier(aIInsight)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aIInsight: PartialUpdateAIInsight): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aIInsight);
    return this.http
      .patch<RestAIInsight>(`${this.resourceUrl}/${this.getAIInsightIdentifier(aIInsight)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAIInsight>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAIInsight[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAIInsightIdentifier(aIInsight: Pick<IAIInsight, 'id'>): number {
    return aIInsight.id;
  }

  compareAIInsight(o1: Pick<IAIInsight, 'id'> | null, o2: Pick<IAIInsight, 'id'> | null): boolean {
    return o1 && o2 ? this.getAIInsightIdentifier(o1) === this.getAIInsightIdentifier(o2) : o1 === o2;
  }

  addAIInsightToCollectionIfMissing<Type extends Pick<IAIInsight, 'id'>>(
    aIInsightCollection: Type[],
    ...aIInsightsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aIInsights: Type[] = aIInsightsToCheck.filter(isPresent);
    if (aIInsights.length > 0) {
      const aIInsightCollectionIdentifiers = aIInsightCollection.map(aIInsightItem => this.getAIInsightIdentifier(aIInsightItem));
      const aIInsightsToAdd = aIInsights.filter(aIInsightItem => {
        const aIInsightIdentifier = this.getAIInsightIdentifier(aIInsightItem);
        if (aIInsightCollectionIdentifiers.includes(aIInsightIdentifier)) {
          return false;
        }
        aIInsightCollectionIdentifiers.push(aIInsightIdentifier);
        return true;
      });
      return [...aIInsightsToAdd, ...aIInsightCollection];
    }
    return aIInsightCollection;
  }

  protected convertDateFromClient<T extends IAIInsight | NewAIInsight | PartialUpdateAIInsight>(aIInsight: T): RestOf<T> {
    return {
      ...aIInsight,
      generatedAt: aIInsight.generatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAIInsight: RestAIInsight): IAIInsight {
    return {
      ...restAIInsight,
      generatedAt: restAIInsight.generatedAt ? dayjs(restAIInsight.generatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAIInsight>): HttpResponse<IAIInsight> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAIInsight[]>): HttpResponse<IAIInsight[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
