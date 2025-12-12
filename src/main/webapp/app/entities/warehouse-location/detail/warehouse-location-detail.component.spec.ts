import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { WarehouseLocationDetailComponent } from './warehouse-location-detail.component';

describe('WarehouseLocation Management Detail Component', () => {
  let comp: WarehouseLocationDetailComponent;
  let fixture: ComponentFixture<WarehouseLocationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WarehouseLocationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./warehouse-location-detail.component').then(m => m.WarehouseLocationDetailComponent),
              resolve: { warehouseLocation: () => of({ id: 22623 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(WarehouseLocationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WarehouseLocationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load warehouseLocation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WarehouseLocationDetailComponent);

      // THEN
      expect(instance.warehouseLocation()).toEqual(expect.objectContaining({ id: 22623 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
