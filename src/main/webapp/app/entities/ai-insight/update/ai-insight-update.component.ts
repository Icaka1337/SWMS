import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IAIInsight } from '../ai-insight.model';
import { AIInsightService } from '../service/ai-insight.service';
import { AIInsightFormGroup, AIInsightFormService } from './ai-insight-form.service';

@Component({
  selector: 'jhi-ai-insight-update',
  templateUrl: './ai-insight-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AIInsightUpdateComponent implements OnInit {
  isSaving = false;
  aIInsight: IAIInsight | null = null;

  productsSharedCollection: IProduct[] = [];

  protected aIInsightService = inject(AIInsightService);
  protected aIInsightFormService = inject(AIInsightFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AIInsightFormGroup = this.aIInsightFormService.createAIInsightFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aIInsight }) => {
      this.aIInsight = aIInsight;
      if (aIInsight) {
        this.updateForm(aIInsight);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aIInsight = this.aIInsightFormService.getAIInsight(this.editForm);
    if (aIInsight.id !== null) {
      this.subscribeToSaveResponse(this.aIInsightService.update(aIInsight));
    } else {
      this.subscribeToSaveResponse(this.aIInsightService.create(aIInsight));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAIInsight>>): void {
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

  protected updateForm(aIInsight: IAIInsight): void {
    this.aIInsight = aIInsight;
    this.aIInsightFormService.resetForm(this.editForm, aIInsight);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      aIInsight.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.aIInsight?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
