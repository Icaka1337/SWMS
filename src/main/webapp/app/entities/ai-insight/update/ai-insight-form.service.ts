import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAIInsight, NewAIInsight } from '../ai-insight.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAIInsight for edit and NewAIInsightFormGroupInput for create.
 */
type AIInsightFormGroupInput = IAIInsight | PartialWithRequiredKeyOf<NewAIInsight>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAIInsight | NewAIInsight> = Omit<T, 'generatedAt'> & {
  generatedAt?: string | null;
};

type AIInsightFormRawValue = FormValueOf<IAIInsight>;

type NewAIInsightFormRawValue = FormValueOf<NewAIInsight>;

type AIInsightFormDefaults = Pick<NewAIInsight, 'id' | 'generatedAt'>;

type AIInsightFormGroupContent = {
  id: FormControl<AIInsightFormRawValue['id'] | NewAIInsight['id']>;
  type: FormControl<AIInsightFormRawValue['type']>;
  message: FormControl<AIInsightFormRawValue['message']>;
  confidence: FormControl<AIInsightFormRawValue['confidence']>;
  generatedAt: FormControl<AIInsightFormRawValue['generatedAt']>;
  product: FormControl<AIInsightFormRawValue['product']>;
};

export type AIInsightFormGroup = FormGroup<AIInsightFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AIInsightFormService {
  createAIInsightFormGroup(aIInsight: AIInsightFormGroupInput = { id: null }): AIInsightFormGroup {
    const aIInsightRawValue = this.convertAIInsightToAIInsightRawValue({
      ...this.getFormDefaults(),
      ...aIInsight,
    });
    return new FormGroup<AIInsightFormGroupContent>({
      id: new FormControl(
        { value: aIInsightRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(aIInsightRawValue.type, {
        validators: [Validators.required],
      }),
      message: new FormControl(aIInsightRawValue.message, {
        validators: [Validators.required],
      }),
      confidence: new FormControl(aIInsightRawValue.confidence),
      generatedAt: new FormControl(aIInsightRawValue.generatedAt, {
        validators: [Validators.required],
      }),
      product: new FormControl(aIInsightRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getAIInsight(form: AIInsightFormGroup): IAIInsight | NewAIInsight {
    return this.convertAIInsightRawValueToAIInsight(form.getRawValue() as AIInsightFormRawValue | NewAIInsightFormRawValue);
  }

  resetForm(form: AIInsightFormGroup, aIInsight: AIInsightFormGroupInput): void {
    const aIInsightRawValue = this.convertAIInsightToAIInsightRawValue({ ...this.getFormDefaults(), ...aIInsight });
    form.reset(
      {
        ...aIInsightRawValue,
        id: { value: aIInsightRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AIInsightFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      generatedAt: currentTime,
    };
  }

  private convertAIInsightRawValueToAIInsight(rawAIInsight: AIInsightFormRawValue | NewAIInsightFormRawValue): IAIInsight | NewAIInsight {
    return {
      ...rawAIInsight,
      generatedAt: dayjs(rawAIInsight.generatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertAIInsightToAIInsightRawValue(
    aIInsight: IAIInsight | (Partial<NewAIInsight> & AIInsightFormDefaults),
  ): AIInsightFormRawValue | PartialWithRequiredKeyOf<NewAIInsightFormRawValue> {
    return {
      ...aIInsight,
      generatedAt: aIInsight.generatedAt ? aIInsight.generatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
