import { Moment } from 'moment';
import { IFeelingIso } from 'app/shared/model/efwservice/feeling-iso.model';

export interface IFeelWheelIso {
    id?: number;
    subject?: string;
    from?: Moment;
    to?: Moment;
    feelings?: IFeelingIso[];
    employeeEmail?: string;
    employeeId?: number;
}

export class FeelWheelIso implements IFeelWheelIso {
    constructor(
        public id?: number,
        public subject?: string,
        public from?: Moment,
        public to?: Moment,
        public feelings?: IFeelingIso[],
        public employeeEmail?: string,
        public employeeId?: number
    ) {}
}
