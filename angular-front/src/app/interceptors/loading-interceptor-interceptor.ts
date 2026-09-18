import { HttpInterceptorFn } from '@angular/common/http';
import { LoadingService } from '../services/loading';
import { inject } from '@angular/core';
import { finalize } from 'rxjs/internal/operators/finalize';

export const loadingInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoadingService);
  loadingService.setLoading(true);
  return next(req).pipe(
    finalize(() => {
      loadingService.setLoading(false);
    })
  );
};
