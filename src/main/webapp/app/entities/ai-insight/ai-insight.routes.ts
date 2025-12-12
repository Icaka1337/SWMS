import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AIInsightResolve from './route/ai-insight-routing-resolve.service';

const aIInsightRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ai-insight.component').then(m => m.AIInsightComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ai-insight-detail.component').then(m => m.AIInsightDetailComponent),
    resolve: {
      aIInsight: AIInsightResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ai-insight-update.component').then(m => m.AIInsightUpdateComponent),
    resolve: {
      aIInsight: AIInsightResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ai-insight-update.component').then(m => m.AIInsightUpdateComponent),
    resolve: {
      aIInsight: AIInsightResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aIInsightRoute;
