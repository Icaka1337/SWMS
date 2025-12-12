import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IWarehouseLocation } from 'app/entities/warehouse-location/warehouse-location.model';
import { WarehouseLocationService } from 'app/entities/warehouse-location/service/warehouse-location.service';
import { ITransaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { TransactionFormService } from './transaction-form.service';

import { TransactionUpdateComponent } from './transaction-update.component';

describe('Transaction Management Update Component', () => {
  let comp: TransactionUpdateComponent;
  let fixture: ComponentFixture<TransactionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionFormService: TransactionFormService;
  let transactionService: TransactionService;
  let productService: ProductService;
  let warehouseLocationService: WarehouseLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransactionUpdateComponent],
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
      .overrideTemplate(TransactionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionFormService = TestBed.inject(TransactionFormService);
    transactionService = TestBed.inject(TransactionService);
    productService = TestBed.inject(ProductService);
    warehouseLocationService = TestBed.inject(WarehouseLocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const transaction: ITransaction = { id: 15110 };
      const product: IProduct = { id: 21536 };
      transaction.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transaction });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('should call WarehouseLocation query and add missing value', () => {
      const transaction: ITransaction = { id: 15110 };
      const sourceLocation: IWarehouseLocation = { id: 22623 };
      transaction.sourceLocation = sourceLocation;
      const targetLocation: IWarehouseLocation = { id: 22623 };
      transaction.targetLocation = targetLocation;

      const warehouseLocationCollection: IWarehouseLocation[] = [{ id: 22623 }];
      jest.spyOn(warehouseLocationService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseLocationCollection })));
      const additionalWarehouseLocations = [sourceLocation, targetLocation];
      const expectedCollection: IWarehouseLocation[] = [...additionalWarehouseLocations, ...warehouseLocationCollection];
      jest.spyOn(warehouseLocationService, 'addWarehouseLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transaction });
      comp.ngOnInit();

      expect(warehouseLocationService.query).toHaveBeenCalled();
      expect(warehouseLocationService.addWarehouseLocationToCollectionIfMissing).toHaveBeenCalledWith(
        warehouseLocationCollection,
        ...additionalWarehouseLocations.map(expect.objectContaining),
      );
      expect(comp.warehouseLocationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const transaction: ITransaction = { id: 15110 };
      const product: IProduct = { id: 21536 };
      transaction.product = product;
      const sourceLocation: IWarehouseLocation = { id: 22623 };
      transaction.sourceLocation = sourceLocation;
      const targetLocation: IWarehouseLocation = { id: 22623 };
      transaction.targetLocation = targetLocation;

      activatedRoute.data = of({ transaction });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContainEqual(product);
      expect(comp.warehouseLocationsSharedCollection).toContainEqual(sourceLocation);
      expect(comp.warehouseLocationsSharedCollection).toContainEqual(targetLocation);
      expect(comp.transaction).toEqual(transaction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransaction>>();
      const transaction = { id: 29476 };
      jest.spyOn(transactionFormService, 'getTransaction').mockReturnValue(transaction);
      jest.spyOn(transactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transaction }));
      saveSubject.complete();

      // THEN
      expect(transactionFormService.getTransaction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionService.update).toHaveBeenCalledWith(expect.objectContaining(transaction));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransaction>>();
      const transaction = { id: 29476 };
      jest.spyOn(transactionFormService, 'getTransaction').mockReturnValue({ id: null });
      jest.spyOn(transactionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transaction }));
      saveSubject.complete();

      // THEN
      expect(transactionFormService.getTransaction).toHaveBeenCalled();
      expect(transactionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransaction>>();
      const transaction = { id: 29476 };
      jest.spyOn(transactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionService.update).toHaveBeenCalled();
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

    describe('compareWarehouseLocation', () => {
      it('should forward to warehouseLocationService', () => {
        const entity = { id: 22623 };
        const entity2 = { id: 1483 };
        jest.spyOn(warehouseLocationService, 'compareWarehouseLocation');
        comp.compareWarehouseLocation(entity, entity2);
        expect(warehouseLocationService.compareWarehouseLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
