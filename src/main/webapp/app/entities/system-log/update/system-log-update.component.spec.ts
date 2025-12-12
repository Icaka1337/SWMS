import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SystemLogService } from '../service/system-log.service';
import { ISystemLog } from '../system-log.model';
import { SystemLogFormService } from './system-log-form.service';

import { SystemLogUpdateComponent } from './system-log-update.component';

describe('SystemLog Management Update Component', () => {
  let comp: SystemLogUpdateComponent;
  let fixture: ComponentFixture<SystemLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let systemLogFormService: SystemLogFormService;
  let systemLogService: SystemLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SystemLogUpdateComponent],
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
      .overrideTemplate(SystemLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SystemLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    systemLogFormService = TestBed.inject(SystemLogFormService);
    systemLogService = TestBed.inject(SystemLogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const systemLog: ISystemLog = { id: 27801 };

      activatedRoute.data = of({ systemLog });
      comp.ngOnInit();

      expect(comp.systemLog).toEqual(systemLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemLog>>();
      const systemLog = { id: 22000 };
      jest.spyOn(systemLogFormService, 'getSystemLog').mockReturnValue(systemLog);
      jest.spyOn(systemLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemLog }));
      saveSubject.complete();

      // THEN
      expect(systemLogFormService.getSystemLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(systemLogService.update).toHaveBeenCalledWith(expect.objectContaining(systemLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemLog>>();
      const systemLog = { id: 22000 };
      jest.spyOn(systemLogFormService, 'getSystemLog').mockReturnValue({ id: null });
      jest.spyOn(systemLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemLog }));
      saveSubject.complete();

      // THEN
      expect(systemLogFormService.getSystemLog).toHaveBeenCalled();
      expect(systemLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemLog>>();
      const systemLog = { id: 22000 };
      jest.spyOn(systemLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(systemLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
