import { DataType } from '../../enums/data-type';
import { WidgetParameterValue } from './widget-parameter-value';

export interface WidgetParameter {
	name: string;
	value: string;
	type: DataType;
	defaultValue: string;
	required: boolean;
	possibleValuesMap: WidgetParameterValue[];
}
