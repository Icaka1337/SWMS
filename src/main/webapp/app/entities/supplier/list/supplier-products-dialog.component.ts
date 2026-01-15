import { Component, OnInit, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from 'app/shared/shared.module';
import { ISupplier } from '../supplier.model';
import { IProduct } from 'app/entities/product/product.model';
import { SupplierService } from '../service/supplier.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-supplier-products-dialog',
  standalone: true,
  imports: [SharedModule, RouterModule],
  template: `
    <div class="modal-header">
      <h4 class="modal-title">Linked Products - {{ supplier?.name }}</h4>
      <button type="button" class="btn-close" aria-label="Close" (click)="cancel()"></button>
    </div>
    <div class="modal-body">
      @if (isLoading) {
        <div class="text-center">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>
      } @else if (products.length === 0) {
        <div class="alert alert-info">
          <fa-icon icon="info-circle"></fa-icon>
          No products are currently linked to this supplier.
        </div>
      } @else {
        <div class="table-responsive">
          <table class="table table-striped table-hover">
            <thead class="table-light">
              <tr>
                <th>SKU</th>
                <th>Name</th>
                <th>Category</th>
                <th>Unit</th>
                <th>Min Stock</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              @for (product of products; track product.id) {
                <tr>
                  <td>
                    <code>{{ product.sku }}</code>
                  </td>
                  <td>
                    <strong>{{ product.name }}</strong>
                  </td>
                  <td>{{ product.category || 'N/A' }}</td>
                  <td>{{ product.unit || 'N/A' }}</td>
                  <td>
                    <span [class]="getStockClass(product.minStock)">
                      {{ product.minStock || 0 }}
                    </span>
                  </td>
                  <td>
                    <a [routerLink]="['/product', product.id, 'view']" class="btn btn-sm btn-info" (click)="cancel()">
                      <fa-icon icon="eye"></fa-icon>
                      View
                    </a>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
        <div class="mt-3"><strong>Total Products: </strong> {{ products.length }}</div>
      }
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" (click)="cancel()">
        <fa-icon icon="times"></fa-icon>
        Close
      </button>
    </div>
  `,
})
export class SupplierProductsDialogComponent implements OnInit {
  supplier?: ISupplier;
  products: IProduct[] = [];
  isLoading = false;

  protected supplierService = inject(SupplierService);
  protected activeModal = inject(NgbActiveModal);

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    if (!this.supplier?.id) {
      return;
    }

    this.isLoading = true;
    this.supplierService.getLinkedProducts(this.supplier.id).subscribe({
      next: response => {
        this.products = response.body ?? [];
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  getStockClass(stock: number | null | undefined): string {
    if (!stock) return 'badge bg-danger';

    if (stock > 50) return 'badge bg-success';
    if (stock > 20) return 'badge bg-warning text-dark';
    return 'badge bg-danger';
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}
