import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAIInsight } from '../ai-insight.model';
import { AIInsightService } from '../service/ai-insight.service';

@Component({
  templateUrl: './ai-insight-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AIInsightDeleteDialogComponent {
  aIInsight?: IAIInsight;

  protected aIInsightService = inject(AIInsightService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aIInsightService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
