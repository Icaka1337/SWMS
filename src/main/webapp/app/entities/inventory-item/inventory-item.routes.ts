import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import InventoryItemResolve from './route/inventory-item-routing-resolve.service';

const inventoryItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inventory-item.component').then(m => m.InventoryItemComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inventory-item-detail.component').then(m => m.InventoryItemDetailComponent),
    resolve: {
      inventoryItem: InventoryItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inventory-item-update.component').then(m => m.InventoryItemUpdateComponent),
    resolve: {
      inventoryItem: InventoryItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inventory-item-update.component').then(m => m.InventoryItemUpdateComponent),
    resolve: {
      inventoryItem: InventoryItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default inventoryItemRoute;
