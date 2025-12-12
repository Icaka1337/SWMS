import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISystemLog } from '../system-log.model';
import { SystemLogService } from '../service/system-log.service';

const systemLogResolve = (route: ActivatedRouteSnapshot): Observable<null | ISystemLog> => {
  const id = route.params.id;
  if (id) {
    return inject(SystemLogService)
      .find(id)
      .pipe(
        mergeMap((systemLog: HttpResponse<ISystemLog>) => {
          if (systemLog.body) {
            return of(systemLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default systemLogResolve;
