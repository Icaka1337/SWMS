import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AIInsightDetailComponent } from './ai-insight-detail.component';

describe('AIInsight Management Detail Component', () => {
  let comp: AIInsightDetailComponent;
  let fixture: ComponentFixture<AIInsightDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AIInsightDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ai-insight-detail.component').then(m => m.AIInsightDetailComponent),
              resolve: { aIInsight: () => of({ id: 695 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AIInsightDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AIInsightDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load aIInsight on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AIInsightDetailComponent);

      // THEN
      expect(instance.aIInsight()).toEqual(expect.objectContaining({ id: 695 }));
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
