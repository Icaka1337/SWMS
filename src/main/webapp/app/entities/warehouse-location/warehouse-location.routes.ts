import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WarehouseLocationResolve from './route/warehouse-location-routing-resolve.service';

const warehouseLocationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/warehouse-location.component').then(m => m.WarehouseLocationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/warehouse-location-detail.component').then(m => m.WarehouseLocationDetailComponent),
    resolve: {
      warehouseLocation: WarehouseLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/warehouse-location-update.component').then(m => m.WarehouseLocationUpdateComponent),
    resolve: {
      warehouseLocation: WarehouseLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/warehouse-location-update.component').then(m => m.WarehouseLocationUpdateComponent),
    resolve: {
      warehouseLocation: WarehouseLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default warehouseLocationRoute;
