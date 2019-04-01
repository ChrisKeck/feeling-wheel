export const enum FeelType {
    ANGRY = 'ANGRY', SAD = 'SAD', HAPPY = 'HAPPY'
}

export interface IFeelingIso {
    id?: number;
    feeltype?: FeelType;
    capacity?: number;
    isSpeechable?: boolean;
    feelwheelSubject?: string;
    feelwheelId?: number;
}

export class FeelingIso implements IFeelingIso {
    constructor(public id?: number,
                public feeltype?: FeelType,
                public capacity?: number,
                public isSpeechable?: boolean,
                public feelwheelSubject?: string,
                public feelwheelId?: number) {
        this.isSpeechable = this.isSpeechable || false;
    }
}
