import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IWarehouseLocation } from 'app/entities/warehouse-location/warehouse-location.model';
import { WarehouseLocationService } from 'app/entities/warehouse-location/service/warehouse-location.service';
import { IInventoryItem } from '../inventory-item.model';
import { InventoryItemService } from '../service/inventory-item.service';
import { InventoryItemFormService } from './inventory-item-form.service';

import { InventoryItemUpdateComponent } from './inventory-item-update.component';

describe('InventoryItem Management Update Component', () => {
  let comp: InventoryItemUpdateComponent;
  let fixture: ComponentFixture<InventoryItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inventoryItemFormService: InventoryItemFormService;
  let inventoryItemService: InventoryItemService;
  let productService: ProductService;
  let warehouseLocationService: WarehouseLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InventoryItemUpdateComponent],
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
      .overrideTemplate(InventoryItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InventoryItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryItemFormService = TestBed.inject(InventoryItemFormService);
    inventoryItemService = TestBed.inject(InventoryItemService);
    productService = TestBed.inject(ProductService);
    warehouseLocationService = TestBed.inject(WarehouseLocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const product: IProduct = { id: 21536 };
      inventoryItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('should call WarehouseLocation query and add missing value', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const location: IWarehouseLocation = { id: 22623 };
      inventoryItem.location = location;

      const warehouseLocationCollection: IWarehouseLocation[] = [{ id: 22623 }];
      jest.spyOn(warehouseLocationService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseLocationCollection })));
      const additionalWarehouseLocations = [location];
      const expectedCollection: IWarehouseLocation[] = [...additionalWarehouseLocations, ...warehouseLocationCollection];
      jest.spyOn(warehouseLocationService, 'addWarehouseLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(warehouseLocationService.query).toHaveBeenCalled();
      expect(warehouseLocationService.addWarehouseLocationToCollectionIfMissing).toHaveBeenCalledWith(
        warehouseLocationCollection,
        ...additionalWarehouseLocations.map(expect.objectContaining),
      );
      expect(comp.warehouseLocationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };
      const product: IProduct = { id: 21536 };
      inventoryItem.product = product;
      const location: IWarehouseLocation = { id: 22623 };
      inventoryItem.location = location;

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContainEqual(product);
      expect(comp.warehouseLocationsSharedCollection).toContainEqual(location);
      expect(comp.inventoryItem).toEqual(inventoryItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryItem>>();
      const inventoryItem = { id: 7462 };
      jest.spyOn(inventoryItemFormService, 'getInventoryItem').mockReturnValue(inventoryItem);
      jest.spyOn(inventoryItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventoryItem }));
      saveSubject.complete();

      // THEN
      expect(inventoryItemFormService.getInventoryItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryItemService.update).toHaveBeenCalledWith(expect.objectContaining(inventoryItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryItem>>();
      const inventoryItem = { id: 7462 };
      jest.spyOn(inventoryItemFormService, 'getInventoryItem').mockReturnValue({ id: null });
      jest.spyOn(inventoryItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventoryItem }));
      saveSubject.complete();

      // THEN
      expect(inventoryItemFormService.getInventoryItem).toHaveBeenCalled();
      expect(inventoryItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryItem>>();
      const inventoryItem = { id: 7462 };
      jest.spyOn(inventoryItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryItemService.update).toHaveBeenCalled();
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
