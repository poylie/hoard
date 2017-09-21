import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DOCUMENT_ROUTE, DocumentComponent } from './';

@NgModule({
    imports: [
        RouterModule.forRoot([ DOCUMENT_ROUTE ], { useHash: true })
    ],
    declarations: [
        DocumentComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterDocumentModule {}
