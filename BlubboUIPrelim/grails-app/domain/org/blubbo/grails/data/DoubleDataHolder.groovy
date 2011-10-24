package org.blubbo.grails.data

import org.blubbo.camel.utils.ConvertType
import org.blubbo.data.IAgedHolder
import org.blubbo.data.IDataTag
import org.blubbo.data.constants.IPropertiesConstants
import org.blubbo.data.impl.DefaultDataHolder

class DoubleDataHolder extends AbstractDataHolder{
	static constraints = {
		value validator: {val,obj ->
				val <= (obj?.maxValue ?: Double.POSITIVE_INFINITY) &&
				val >= (obj?.minValue ?: Double.NEGATIVE_INFINITY)
		}
		minValue(nullable:true)
		maxValue(nullable:true)
	}
	
	static DoubleDataHolder subHolderFromCSVMap(Map map){
		DoubleDataHolder dataHolder = new DoubleDataHolder()
		String dataClass = map['CLASS']
		dataHolder.valueClassName = dataClass
		dataHolder.value = map['VALUE'] as Double
		dataHolder.timestamp = map['TIMESTAMP'] ? map['TIMESTAMP'] as Long : (new Date()).getTime()
		def dataHolderProps = dataHolder.properties.keySet()
		if ('minValue' in dataHolderProps && map['MIN_VALUE']){
			dataHolder.minValue = map['MIN_VALUE'] as Long
		}
		if ('maxValue' in dataHolderProps && map['MAX_VALUE']){
			dataHolder.maxValue = map['MAX_VALUE'] as Long
		}
		if ('defaultValue' in dataHolderProps && map['DEFAULT_VALUE']){
			dataHolder.defaultValue = map['DEFAULT_VALUE'] as Long
		}
		return dataHolder
	}
/*	
	public static DoubleConstraint FLOAT_CONSTRAINT =
		new DoubleConstraint(Double.valueOf(Float.MIN_VALUE),Double.valueOf(Float.MAX_VALUE));
	public static DoubleConstraint DOUBLE_CONSTRAINT =
		new DoubleConstraint(Double.MIN_VALUE,Double.MAX_VALUE);
*/	
	
	//DoubleConstraint constraint
	
	public static Double getMaxConstraint(Class clazz){
		switch (clazz){
			case Float : return Float.MAX_VALUE;
			case Double : 
			default: return Double.MAX_VALUE;
		}
	}
	
	public static Double getMinConstraint(Class clazz){
		switch (clazz){
			case Float : return Float.MIN_VALUE;
			case Double : 
			default: return Double.MIN_VALUE;
		}
	}
	
	Double value
	Double maxValue
	Double minValue
	Double defaultValue = 0d
	
	DoubleDataHolder(){}
	
	DoubleDataHolder(IDataTag<?> dataTag){
		super(dataTag);
		Double maxVal = dataTag.getProperty(IPropertiesConstants.MAX_VALUE_KEY, Double.class);
		Double minVal = dataTag.getProperty(IPropertiesConstants.MIN_VALUE_KEY, Double.class);
		
		setMaxValue(maxVal)
		setMinValue(minVal)
		
		value = dataTag.getValue()
	}
	
	public boolean checkSjDataTagCurrent(IDataTag<?> sjTag){
		if (value != sjTag.getValue()) { return false }
		if (!super.checkSjDataTagCurrent(sjTag)) { return false }
		return true
	}
	
	void updateFromDataHolder(IAgedHolder<?> dataHolder){
		super.updateFromDataHolder(dataHolder)
		this.value = dataHolder.value
	}
	
	protected DefaultDataHolder<?> newDataHolder(){
		super.newDataHolder()
		Class<?> clazz = valueClass;
		def typedValue = ConvertType.convertTo(clazz,value)
		return DefaultDataHolder.getNewInstance(clazz, typedValue, timestamp)
	}
	
	void setMaxValue(Double maxValue){
		this.maxValue = maxValue; // will validate constraint after class is set.
		validateConstraints()
	}
	
	void setMinValue(Double minValue){
		this.minValue = minValue; // will validate constraint after class is set.
		validateConstraints()
	}
	
	void validateConstraints(){
		if (valueClass == null){
			return;
		}
			// Make sure min value is within valueClass range.
		def defaultMin = getMinConstraint(qualifiedClassFromValueClassName())
		this.minValue = minValue != null && minValue >= defaultMin ? minValue : defaultMin;
		
			// Make sure max value is within valueClass range.
		def defaultMax = getMaxConstraint(qualifiedClassFromValueClassName())
		this.maxValue = maxValue != null && maxValue <= defaultMax ? maxValue : defaultMax;
	}
	
	void setValueClass(Class valueClass){
		if (valueClass != this.valueClass){
			super.setValueClass valueClass
			validateConstraints();
		}
	}
	
	void setValueClassName(String valueClassName){
		if (this.valueClassName != valueClassName){
			super.setValueClassName valueClassName
			validateConstraints()
		}
	}
	
	void setValue(Double value){
		if (this.value != value){
			setSjDataHolderStale(true)
			this.value = value
		}
	}
	
	DoubleDataHolder copy(){
		def target = new DoubleDataHolder()
		super.copyFields(target)
		target.value = value
		target.maxValue = maxValue
		target.minValue = minValue
		return target
	}
	
	String getValueString(){
		value.toString()
	}
	
	String toString(){
		return "$value ($valueClass)"
	}
	
	static belongsTo = DataTag
	
	
	
}
