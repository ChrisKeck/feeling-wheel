import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {EfwgatewayAppModule} from './app.module';
import {ProdConfig} from './blocks/config/prod.config';

ProdConfig();

if (module['hot']) {
    module['hot'].accept();
}

platformBrowserDynamic()
    .bootstrapModule(EfwgatewayAppModule, {preserveWhitespaces: true})
    .then(success => console.log(`Application started`))
    .catch(err => console.error(err));
