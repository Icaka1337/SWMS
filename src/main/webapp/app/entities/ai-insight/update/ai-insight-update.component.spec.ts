import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { AIInsightService } from '../service/ai-insight.service';
import { IAIInsight } from '../ai-insight.model';
import { AIInsightFormService } from './ai-insight-form.service';

import { AIInsightUpdateComponent } from './ai-insight-update.component';

describe('AIInsight Management Update Component', () => {
  let comp: AIInsightUpdateComponent;
  let fixture: ComponentFixture<AIInsightUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aIInsightFormService: AIInsightFormService;
  let aIInsightService: AIInsightService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AIInsightUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AIInsightUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AIInsightUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aIInsightFormService = TestBed.inject(AIInsightFormService);
    aIInsightService = TestBed.inject(AIInsightService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const aIInsight: IAIInsight = { id: 28792 };
      const product: IProduct = { id: 21536 };
      aIInsight.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aIInsight });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const aIInsight: IAIInsight = { id: 28792 };
      const product: IProduct = { id: 21536 };
      aIInsight.product = product;

      activatedRoute.data = of({ aIInsight });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContainEqual(product);
      expect(comp.aIInsight).toEqual(aIInsight);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAIInsight>>();
      const aIInsight = { id: 695 };
      jest.spyOn(aIInsightFormService, 'getAIInsight').mockReturnValue(aIInsight);
      jest.spyOn(aIInsightService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aIInsight });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aIInsight }));
      saveSubject.complete();

      // THEN
      expect(aIInsightFormService.getAIInsight).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aIInsightService.update).toHaveBeenCalledWith(expect.objectContaining(aIInsight));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAIInsight>>();
      const aIInsight = { id: 695 };
      jest.spyOn(aIInsightFormService, 'getAIInsight').mockReturnValue({ id: null });
      jest.spyOn(aIInsightService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aIInsight: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aIInsight }));
      saveSubject.complete();

      // THEN
      expect(aIInsightFormService.getAIInsight).toHaveBeenCalled();
      expect(aIInsightService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAIInsight>>();
      const aIInsight = { id: 695 };
      jest.spyOn(aIInsightService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aIInsight });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aIInsightService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
