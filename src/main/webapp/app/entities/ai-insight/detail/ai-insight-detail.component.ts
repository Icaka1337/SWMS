import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAIInsight } from '../ai-insight.model';

@Component({
  selector: 'jhi-ai-insight-detail',
  templateUrl: './ai-insight-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AIInsightDetailComponent {
  aIInsight = input<IAIInsight | null>(null);

  previousState(): void {
    window.history.back();
  }
}
