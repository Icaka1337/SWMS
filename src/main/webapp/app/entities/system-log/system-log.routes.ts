import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SystemLogResolve from './route/system-log-routing-resolve.service';

const systemLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/system-log.component').then(m => m.SystemLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/system-log-detail.component').then(m => m.SystemLogDetailComponent),
    resolve: {
      systemLog: SystemLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/system-log-update.component').then(m => m.SystemLogUpdateComponent),
    resolve: {
      systemLog: SystemLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/system-log-update.component').then(m => m.SystemLogUpdateComponent),
    resolve: {
      systemLog: SystemLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default systemLogRoute;
