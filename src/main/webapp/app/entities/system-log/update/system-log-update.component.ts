import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISystemLog } from '../system-log.model';
import { SystemLogService } from '../service/system-log.service';
import { SystemLogFormGroup, SystemLogFormService } from './system-log-form.service';

@Component({
  selector: 'jhi-system-log-update',
  templateUrl: './system-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SystemLogUpdateComponent implements OnInit {
  isSaving = false;
  systemLog: ISystemLog | null = null;

  protected systemLogService = inject(SystemLogService);
  protected systemLogFormService = inject(SystemLogFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SystemLogFormGroup = this.systemLogFormService.createSystemLogFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemLog }) => {
      this.systemLog = systemLog;
      if (systemLog) {
        this.updateForm(systemLog);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemLog = this.systemLogFormService.getSystemLog(this.editForm);
    if (systemLog.id !== null) {
      this.subscribeToSaveResponse(this.systemLogService.update(systemLog));
    } else {
      this.subscribeToSaveResponse(this.systemLogService.create(systemLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemLog>>): void {
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

  protected updateForm(systemLog: ISystemLog): void {
    this.systemLog = systemLog;
    this.systemLogFormService.resetForm(this.editForm, systemLog);
  }
}
