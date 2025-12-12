import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInventoryItem } from '../inventory-item.model';
import { InventoryItemService } from '../service/inventory-item.service';

const inventoryItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IInventoryItem> => {
  const id = route.params.id;
  if (id) {
    return inject(InventoryItemService)
      .find(id)
      .pipe(
        mergeMap((inventoryItem: HttpResponse<IInventoryItem>) => {
          if (inventoryItem.body) {
            return of(inventoryItem.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default inventoryItemResolve;
