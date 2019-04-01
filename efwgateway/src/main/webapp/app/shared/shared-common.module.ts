import {NgModule} from '@angular/core';

import {EfwgatewaySharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent} from './';

@NgModule({
              imports: [EfwgatewaySharedLibsModule],
              declarations: [FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent],
              exports: [EfwgatewaySharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent]
          })
export class EfwgatewaySharedCommonModule {
}
