import { Component, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject, forkJoin, of } from 'rxjs';
import { catchError, finalize, map, takeUntil } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);
  products = signal<IProduct[]>([]);
  transactions = signal<ITransaction[]>([]);
  inventoryItems = signal<IInventoryItem[]>([]);
  isLoading = signal(false);

  totalProducts = computed(() => this.products().length);
  inventoryByProduct = computed(() => this.buildInventoryByProduct(this.inventoryItems()));
  stockQuantity = computed(() => this.buildStockQuantity(this.inventoryByProduct()));
  lowStockCount = computed(() => this.buildLowStockCount(this.inventoryByProduct()));
  overStockCount = computed(() => this.buildOverStockCount(this.inventoryByProduct()));

  flowSeries = computed(() => this.buildFlowSeries());
  flowBars = computed(() => this.buildFlowBars(this.flowSeries()));
  flowSummary = computed(() => this.buildFlowSummary(this.flowSeries()));

  categoryLegend = computed(() => this.buildCategoryLegend());
  categoryGradient = computed(() => this.buildCategoryGradient(this.categoryLegend()));
  topCategory = computed(() => this.categoryLegend()[0] ?? null);

  aiInsights = computed(() => this.buildAiInsights());
  aiHighlights = computed(() => this.aiInsights().slice(0, 2));

  healthStatus = computed(() => this.buildHealthStatus());

  private readonly destroy$ = new Subject<void>();

  private readonly accountService = inject(AccountService);
  private readonly router = inject(Router);
  private readonly productService = inject(ProductService);
  private readonly transactionService = inject(TransactionService);
  private readonly inventoryItemService = inject(InventoryItemService);

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));

    this.loadDashboardData();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadDashboardData(): void {
    this.isLoading.set(true);
    forkJoin({
      products: this.productService.query({ page: 0, size: 10000, sort: ['id,asc'] }).pipe(
        map(res => res.body ?? []),
        catchError(() => of([])),
      ),
      transactions: this.transactionService.query({ page: 0, size: 10000, sort: ['date,desc'] }).pipe(
        map(res => res.body ?? []),
        catchError(() => of([])),
      ),
      inventoryItems: this.inventoryItemService.query({ page: 0, size: 10000, sort: ['id,asc'] }).pipe(
        map(res => res.body ?? []),
        catchError(() => of([])),
      ),
    })
      .pipe(
        finalize(() => this.isLoading.set(false)),
        takeUntil(this.destroy$),
      )
      .subscribe(({ products, transactions, inventoryItems }) => {
        this.products.set(products);
        this.transactions.set(transactions);
        this.inventoryItems.set(inventoryItems);
      });
  }

  private buildInventoryByProduct(items: IInventoryItem[]): Map<number, number> {
    const inventory = new Map<number, number>();
    for (const item of items) {
      if (!item.product?.id) {
        continue;
      }
      const quantity = item.quantity ?? 0;
      if (!quantity) {
        continue;
      }
      const current = inventory.get(item.product.id) ?? 0;
      inventory.set(item.product.id, current + quantity);
    }
    return inventory;
  }

  private buildStockQuantity(inventory: Map<number, number>): number {
    let total = 0;
    for (const value of inventory.values()) {
      total += value;
    }
    return total;
  }

  private buildLowStockCount(inventory: Map<number, number>): number {
    let count = 0;
    for (const product of this.products()) {
      const minStock = product.minStock ?? null;
      if (minStock === null) {
        continue;
      }
      const current = inventory.get(product.id) ?? 0;
      if (current < minStock) {
        count += 1;
      }
    }
    return count;
  }

  private buildOverStockCount(inventory: Map<number, number>): number {
    let count = 0;
    for (const product of this.products()) {
      const maxStock = product.maxStock ?? null;
      if (maxStock === null) {
        continue;
      }
      const current = inventory.get(product.id) ?? 0;
      if (current > maxStock) {
        count += 1;
      }
    }
    return count;
  }

  private buildFlowSeries(): { inbound: number[]; outbound: number[] } {
    const days = 6;
    const inbound: number[] = Array.from<number>({ length: days });
    const outbound: number[] = Array.from<number>({ length: days });
    const startDay = dayjs()
      .startOf('day')
      .subtract(days - 1, 'day');

    for (const transaction of this.transactions()) {
      if (!transaction.date) {
        continue;
      }
      const dayIndex = transaction.date.startOf('day').diff(startDay, 'day');
      if (dayIndex < 0 || dayIndex >= days) {
        continue;
      }
      const quantity = Math.abs(transaction.quantity ?? 0);
      if (!quantity) {
        continue;
      }
      if (this.isIncomingTransaction(transaction)) {
        inbound[dayIndex] += quantity;
      } else if (this.isOutgoingTransaction(transaction)) {
        outbound[dayIndex] += quantity;
      }
    }

    return { inbound, outbound };
  }

  private buildFlowBars(series: { inbound: number[]; outbound: number[] }): { height: number; accent: boolean }[] {
    const maxValue = Math.max(1, ...series.inbound, ...series.outbound);
    const bars: { height: number; accent: boolean }[] = [];

    for (let i = 0; i < series.inbound.length; i += 1) {
      bars.push({ height: Math.round((series.inbound[i] / maxValue) * 100), accent: false });
      bars.push({ height: Math.round((series.outbound[i] / maxValue) * 100), accent: true });
    }

    return bars;
  }

  private buildFlowSummary(series: { inbound: number[]; outbound: number[] }): { incoming: number; outgoing: number } {
    const incoming = series.inbound.reduce((sum, value) => sum + value, 0);
    const outgoing = series.outbound.reduce((sum, value) => sum + value, 0);
    return { incoming, outgoing };
  }

  private buildCategoryLegend(): { name: string; count: number; percent: number; dotClass: string }[] {
    const counts = new Map<string, number>();
    for (const product of this.products()) {
      const name = (product.category ?? 'Uncategorized').trim() || 'Uncategorized';
      counts.set(name, (counts.get(name) ?? 0) + 1);
    }

    const total = this.products().length || 1;
    const colorClasses = [
      'swms-dot swms-dot--cat-1',
      'swms-dot swms-dot--cat-2',
      'swms-dot swms-dot--cat-3',
      'swms-dot swms-dot--cat-4',
      'swms-dot swms-dot--cat-5',
      'swms-dot swms-dot--cat-6',
      'swms-dot swms-dot--cat-7',
      'swms-dot swms-dot--cat-8',
      'swms-dot swms-dot--cat-9',
      'swms-dot swms-dot--cat-10',
    ];

    return Array.from(counts.entries())
      .sort((a, b) => b[1] - a[1])
      .slice(0, 10)
      .map(([name, count], index) => ({
        name,
        count,
        percent: Math.round((count / total) * 100),
        dotClass: colorClasses[index] ?? 'swms-dot swms-dot--secondary',
      }));
  }

  private buildCategoryGradient(legend: { name: string; count: number; percent: number }[]): string {
    if (!legend.length) {
      return 'conic-gradient(#e9ecef 0deg 360deg)';
    }

    const colors = ['#0d6efd', '#198754', '#f59f00', '#DC3545', '#6F42C1', '#0DC9E0', '#d900ffff', '#00ff4cff', '#d0ff00ff', '#ff007bff'];
    const total = legend.reduce((sum, item) => sum + item.count, 0) || 1;
    let current = 0;
    const segments = legend.map((item, index) => {
      const percent = (item.count / total) * 100;
      const start = current;
      const end = current + percent;
      current = end;
      return `${colors[index] ?? '#6c757d'} ${start}% ${end}%`;
    });

    if (current < 100) {
      segments.push(`#e9ecef ${current}% 100%`);
    }

    return `conic-gradient(${segments.join(', ')})`;
  }

  private buildAiInsights(): string[] {
    const insights: string[] = [];
    const lowStock = this.lowStockCount();
    const overStock = this.overStockCount();
    const flow = this.flowSummary();

    if (lowStock > 0) {
      insights.push(`${lowStock} SKUs below min stock. Trigger replenishment.`);
    }
    if (overStock > 0) {
      insights.push(`${overStock} SKUs above max stock. Rebalance locations.`);
    }
    if (flow.incoming > flow.outgoing * 1.2) {
      insights.push('Inbound volume is higher than outbound this week. Plan outbound capacity.');
    } else if (flow.outgoing > flow.incoming * 1.2) {
      insights.push('Outbound demand is higher than inbound. Review reorder levels.');
    }

    if (!insights.length) {
      insights.push('Stock levels are within thresholds. No urgent actions detected.');
    }

    return insights.slice(0, 3);
  }

  private buildHealthStatus(): { label: string; className: string } {
    if (this.lowStockCount() > 0 || this.overStockCount() > 0) {
      return { label: 'Needs attention', className: 'bg-warning text-dark' };
    }
    return { label: 'Stable', className: 'bg-success text-white' };
  }

  private getTransactionDirection(transaction: ITransaction): number {
    if (this.isIncomingTransaction(transaction)) {
      return 1;
    }
    if (this.isOutgoingTransaction(transaction)) {
      return -1;
    }
    return 0;
  }

  private isIncomingTransaction(transaction: ITransaction): boolean {
    const type = (transaction.type ?? '').toLowerCase();
    return type.includes('in') || type.includes('receipt') || type.includes('receive') || type.includes('purchase');
  }

  private isOutgoingTransaction(transaction: ITransaction): boolean {
    const type = (transaction.type ?? '').toLowerCase();
    return type.includes('out') || type.includes('ship') || type.includes('issue') || type.includes('dispatch');
  }
}
