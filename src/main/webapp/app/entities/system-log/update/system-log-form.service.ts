import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISystemLog, NewSystemLog } from '../system-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISystemLog for edit and NewSystemLogFormGroupInput for create.
 */
type SystemLogFormGroupInput = ISystemLog | PartialWithRequiredKeyOf<NewSystemLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISystemLog | NewSystemLog> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type SystemLogFormRawValue = FormValueOf<ISystemLog>;

type NewSystemLogFormRawValue = FormValueOf<NewSystemLog>;

type SystemLogFormDefaults = Pick<NewSystemLog, 'id' | 'timestamp'>;

type SystemLogFormGroupContent = {
  id: FormControl<SystemLogFormRawValue['id'] | NewSystemLog['id']>;
  username: FormControl<SystemLogFormRawValue['username']>;
  action: FormControl<SystemLogFormRawValue['action']>;
  entityName: FormControl<SystemLogFormRawValue['entityName']>;
  details: FormControl<SystemLogFormRawValue['details']>;
  timestamp: FormControl<SystemLogFormRawValue['timestamp']>;
};

export type SystemLogFormGroup = FormGroup<SystemLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SystemLogFormService {
  createSystemLogFormGroup(systemLog: SystemLogFormGroupInput = { id: null }): SystemLogFormGroup {
    const systemLogRawValue = this.convertSystemLogToSystemLogRawValue({
      ...this.getFormDefaults(),
      ...systemLog,
    });
    return new FormGroup<SystemLogFormGroupContent>({
      id: new FormControl(
        { value: systemLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl(systemLogRawValue.username),
      action: new FormControl(systemLogRawValue.action),
      entityName: new FormControl(systemLogRawValue.entityName),
      details: new FormControl(systemLogRawValue.details),
      timestamp: new FormControl(systemLogRawValue.timestamp, {
        validators: [Validators.required],
      }),
    });
  }

  getSystemLog(form: SystemLogFormGroup): ISystemLog | NewSystemLog {
    return this.convertSystemLogRawValueToSystemLog(form.getRawValue() as SystemLogFormRawValue | NewSystemLogFormRawValue);
  }

  resetForm(form: SystemLogFormGroup, systemLog: SystemLogFormGroupInput): void {
    const systemLogRawValue = this.convertSystemLogToSystemLogRawValue({ ...this.getFormDefaults(), ...systemLog });
    form.reset(
      {
        ...systemLogRawValue,
        id: { value: systemLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SystemLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertSystemLogRawValueToSystemLog(rawSystemLog: SystemLogFormRawValue | NewSystemLogFormRawValue): ISystemLog | NewSystemLog {
    return {
      ...rawSystemLog,
      timestamp: dayjs(rawSystemLog.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertSystemLogToSystemLogRawValue(
    systemLog: ISystemLog | (Partial<NewSystemLog> & SystemLogFormDefaults),
  ): SystemLogFormRawValue | PartialWithRequiredKeyOf<NewSystemLogFormRawValue> {
    return {
      ...systemLog,
      timestamp: systemLog.timestamp ? systemLog.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
