import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'swmsApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'swmsApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'supplier',
    data: { pageTitle: 'swmsApp.supplier.home.title' },
    loadChildren: () => import('./supplier/supplier.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'swmsApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'warehouse-location',
    data: { pageTitle: 'swmsApp.warehouseLocation.home.title' },
    loadChildren: () => import('./warehouse-location/warehouse-location.routes'),
  },
  {
    path: 'inventory-item',
    data: { pageTitle: 'swmsApp.inventoryItem.home.title' },
    loadChildren: () => import('./inventory-item/inventory-item.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'swmsApp.transaction.home.title' },
    loadChildren: () => import('./transaction/transaction.routes'),
  },
  {
    path: 'ai-insight',
    data: { pageTitle: 'swmsApp.aIInsight.home.title' },
    loadChildren: () => import('./ai-insight/ai-insight.routes'),
  },
  {
    path: 'system-log',
    data: { pageTitle: 'swmsApp.systemLog.home.title' },
    loadChildren: () => import('./system-log/system-log.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
