import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LoadingService {
    private readonly loading = signal(false);

    public readonly isLoading = this.loading.asReadonly();

    public setLoading(value: boolean) {
        this.loading.set(value);
    }
}
