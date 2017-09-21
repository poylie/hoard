import { Route } from '@angular/router';

import { UserRouteAccessService } from '../shared';
import { DocumentComponent } from './';

export const DOCUMENT_ROUTE: Route = {
  path: 'document',
  component: DocumentComponent,
  data: {
    authorities: [],
    pageTitle: 'document.title'
  },
  canActivate: [UserRouteAccessService]
};
