import {JhiTrackerService} from 'app/core/tracker/tracker.service';
import {SpyObject} from './spyobject';

export class MockTrackerService extends SpyObject {
    constructor() {
        super(JhiTrackerService);
    }

    connect() {
    }
}
