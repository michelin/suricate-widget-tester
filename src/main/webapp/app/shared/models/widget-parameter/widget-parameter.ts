import { DataTypeEnum } from '../../enums/data-type.enum';
import { WidgetParameterValue } from './widget-parameter-value';

export interface WidgetParameter {
	name: string;
	value: string;
	type: DataTypeEnum;
	defaultValue: string;
	required: boolean;
	possibleValuesMap: WidgetParameterValue[];
}
