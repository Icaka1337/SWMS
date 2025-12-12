import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IWarehouseLocation } from 'app/entities/warehouse-location/warehouse-location.model';
import { WarehouseLocationService } from 'app/entities/warehouse-location/service/warehouse-location.service';
import { TransactionService } from '../service/transaction.service';
import { ITransaction } from '../transaction.model';
import { TransactionFormGroup, TransactionFormService } from './transaction-form.service';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  transaction: ITransaction | null = null;

  productsSharedCollection: IProduct[] = [];
  warehouseLocationsSharedCollection: IWarehouseLocation[] = [];

  protected transactionService = inject(TransactionService);
  protected transactionFormService = inject(TransactionFormService);
  protected productService = inject(ProductService);
  protected warehouseLocationService = inject(WarehouseLocationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionFormGroup = this.transactionFormService.createTransactionFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareWarehouseLocation = (o1: IWarehouseLocation | null, o2: IWarehouseLocation | null): boolean =>
    this.warehouseLocationService.compareWarehouseLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
      if (transaction) {
        this.updateForm(transaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.transactionFormService.getTransaction(this.editForm);
    if (transaction.id !== null) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(transaction: ITransaction): void {
    this.transaction = transaction;
    this.transactionFormService.resetForm(this.editForm, transaction);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      transaction.product,
    );
    this.warehouseLocationsSharedCollection = this.warehouseLocationService.addWarehouseLocationToCollectionIfMissing<IWarehouseLocation>(
      this.warehouseLocationsSharedCollection,
      transaction.sourceLocation,
      transaction.targetLocation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.transaction?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.warehouseLocationService
      .query()
      .pipe(map((res: HttpResponse<IWarehouseLocation[]>) => res.body ?? []))
      .pipe(
        map((warehouseLocations: IWarehouseLocation[]) =>
          this.warehouseLocationService.addWarehouseLocationToCollectionIfMissing<IWarehouseLocation>(
            warehouseLocations,
            this.transaction?.sourceLocation,
            this.transaction?.targetLocation,
          ),
        ),
      )
      .subscribe((warehouseLocations: IWarehouseLocation[]) => (this.warehouseLocationsSharedCollection = warehouseLocations));
  }
}
