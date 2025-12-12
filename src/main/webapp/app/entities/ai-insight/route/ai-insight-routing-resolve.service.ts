import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAIInsight } from '../ai-insight.model';
import { AIInsightService } from '../service/ai-insight.service';

const aIInsightResolve = (route: ActivatedRouteSnapshot): Observable<null | IAIInsight> => {
  const id = route.params.id;
  if (id) {
    return inject(AIInsightService)
      .find(id)
      .pipe(
        mergeMap((aIInsight: HttpResponse<IAIInsight>) => {
          if (aIInsight.body) {
            return of(aIInsight.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aIInsightResolve;
