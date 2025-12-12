import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWarehouseLocation } from '../warehouse-location.model';
import { WarehouseLocationService } from '../service/warehouse-location.service';

const warehouseLocationResolve = (route: ActivatedRouteSnapshot): Observable<null | IWarehouseLocation> => {
  const id = route.params.id;
  if (id) {
    return inject(WarehouseLocationService)
      .find(id)
      .pipe(
        mergeMap((warehouseLocation: HttpResponse<IWarehouseLocation>) => {
          if (warehouseLocation.body) {
            return of(warehouseLocation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default warehouseLocationResolve;
