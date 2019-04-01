import {JhiAlert, JhiAlertService} from 'ng-jhipster';
import {SpyObject} from './spyobject';

export class MockAlertService extends SpyObject {
    constructor() {
        super(JhiAlertService);
    }

    addAlert(alertOptions: JhiAlert) {
        return alertOptions;
    }
}
