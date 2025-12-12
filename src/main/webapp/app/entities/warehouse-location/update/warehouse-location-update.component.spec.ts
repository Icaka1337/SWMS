import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { WarehouseLocationService } from '../service/warehouse-location.service';
import { IWarehouseLocation } from '../warehouse-location.model';
import { WarehouseLocationFormService } from './warehouse-location-form.service';

import { WarehouseLocationUpdateComponent } from './warehouse-location-update.component';

describe('WarehouseLocation Management Update Component', () => {
  let comp: WarehouseLocationUpdateComponent;
  let fixture: ComponentFixture<WarehouseLocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let warehouseLocationFormService: WarehouseLocationFormService;
  let warehouseLocationService: WarehouseLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WarehouseLocationUpdateComponent],
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
      .overrideTemplate(WarehouseLocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WarehouseLocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    warehouseLocationFormService = TestBed.inject(WarehouseLocationFormService);
    warehouseLocationService = TestBed.inject(WarehouseLocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const warehouseLocation: IWarehouseLocation = { id: 1483 };

      activatedRoute.data = of({ warehouseLocation });
      comp.ngOnInit();

      expect(comp.warehouseLocation).toEqual(warehouseLocation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarehouseLocation>>();
      const warehouseLocation = { id: 22623 };
      jest.spyOn(warehouseLocationFormService, 'getWarehouseLocation').mockReturnValue(warehouseLocation);
      jest.spyOn(warehouseLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warehouseLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warehouseLocation }));
      saveSubject.complete();

      // THEN
      expect(warehouseLocationFormService.getWarehouseLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(warehouseLocationService.update).toHaveBeenCalledWith(expect.objectContaining(warehouseLocation));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarehouseLocation>>();
      const warehouseLocation = { id: 22623 };
      jest.spyOn(warehouseLocationFormService, 'getWarehouseLocation').mockReturnValue({ id: null });
      jest.spyOn(warehouseLocationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warehouseLocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warehouseLocation }));
      saveSubject.complete();

      // THEN
      expect(warehouseLocationFormService.getWarehouseLocation).toHaveBeenCalled();
      expect(warehouseLocationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarehouseLocation>>();
      const warehouseLocation = { id: 22623 };
      jest.spyOn(warehouseLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warehouseLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(warehouseLocationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
