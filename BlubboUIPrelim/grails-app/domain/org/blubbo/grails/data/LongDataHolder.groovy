package org.blubbo.grails.data

import java.util.Map

import org.blubbo.camel.utils.ConvertType
import org.blubbo.data.IAgedHolder
import org.blubbo.data.IDataTag
import org.blubbo.data.constants.IPropertiesConstants
import org.blubbo.data.impl.DefaultDataHolder

class LongDataHolder extends AbstractDataHolder{
	static belongsTo = DataTag
    static constraints = {
		value validator: {val,obj -> 
				//val <= (obj?.constraint?.maxValue ?: Long.MAX_VALUE) &&
				//val >= (obj?.constraint?.minValue ?: Long.MIN_VALUE)
			val <= (obj?.maxValue ?: Long.MAX_VALUE) &&
			val >= (obj?.minValue ?: Long.MIN_VALUE)
		}
		maxValue(nullable:true)
		minValue(nullable:true)
    }
	
	static LongDataHolder subHolderFromCSVMap(Map map){
		LongDataHolder dataHolder = new LongDataHolder()
		String dataClass = map['CLASS']
		dataHolder.valueClassName = dataClass
		dataHolder.value = map['VALUE'] as Long
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
	public static LongIntConstraint BYTE_CONSTRAINT =
		new LongIntConstraint(Long.valueOf(Byte.MIN_VALUE),Long.valueOf(Byte.MAX_VALUE));
	public static LongIntConstraint SHORT_CONSTRAINT =
		new LongIntConstraint(Long.valueOf(Short.MIN_VALUE),Long.valueOf(Short.MAX_VALUE));
	public static LongIntConstraint INTEGER_CONSTRAINT =
		new LongIntConstraint(Long.valueOf(Integer.MIN_VALUE),Long.valueOf(Integer.MAX_VALUE));
	public static LongIntConstraint LONG_CONSTRAINT =
		new LongIntConstraint(Long.valueOf(Long.MIN_VALUE),Long.valueOf(Long.MAX_VALUE));
*/	
	public static Long getMaxConstraint(Class clazz){
		switch (clazz){
			case Byte : return Byte.MAX_VALUE;
			case Short : return Short.MAX_VALUE;
			case Integer : return Integer.MAX_VALUE;
			case Long : return Long.MAX_VALUE;
		}
	}
	
	public static Long getMinConstraint(Class clazz){
		switch (clazz){
			case Byte : return Byte.MIN_VALUE;
			case Short : return Short.MIN_VALUE;
			case Integer : return Integer.MIN_VALUE;
			case Long : return Long.MIN_VALUE;
		}
	}
		
	//LongIntConstraint constraint
	
	Long value
	Long maxValue
	Long minValue
	Long defaultValue = 0L
	
	
	LongDataHolder(){}
	
	LongDataHolder(IDataTag<?> dataTag){
		super(dataTag)
		
		Long maxVal = dataTag.getProperty(IPropertiesConstants.MAX_VALUE_KEY, Long.class);
		Long minVal = dataTag.getProperty(IPropertiesConstants.MIN_VALUE_KEY, Long.class);
		
		setMaxValue(maxVal)
		setMinValue(minVal)
		
		value = dataTag.getValue()
		
	}
	
	void setMaxValue(Long maxValue){
		this.maxValue = maxValue; // will validate constraint after class is set.
		validateConstraints()
	}
	
	void setMinValue(Long minValue){
		this.minValue = minValue; // will validate constraint after class is set.
		validateConstraints()
	}
	
	void validateConstraints(){
		if (valueClass == null){
			return;
		}
			// Make sure min value is within valueClass range.
		def defaultMin = getMinConstraint(qualifiedClassFromValueClassName()) ?: Long.MIN_VALUE
		this.minValue = minValue != null && minValue >= defaultMin ? minValue : null;
		
			// Make sure max value is within valueClass range.
		def defaultMax = getMaxConstraint(qualifiedClassFromValueClassName()) ?: Long.MAX_VALUE
		this.maxValue = maxValue != null && maxValue <= defaultMax ? maxValue : null;
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
	
	void setValue(Long value){
		if (this.value != value){
			setSjDataHolderStale('setValue',true)
			this.value = value
		}
	}
	
	public boolean checkSjDataTagCurrent(IDataTag<?> sjTag){
		if (value != sjTag.getValue()) { return false }
		if (!super.checkSjDataTagCurrent(sjTag)) { return false }
		return true
	}
	
	void updateFromDataHolder(IAgedHolder<?> dataHolder){
		log.info "Updating data holder with value ${value} to ${dataHolder.value}"
		super.updateFromDataHolder(dataHolder)
		this.value = dataHolder.value
		save(flush:true)
	}
	
	protected DefaultDataHolder<?> newDataHolder(){
		super.newDataHolder()
		Class<?> clazz = valueClass;
		def typedValue = ConvertType.convertTo(clazz,value)
		return DefaultDataHolder.getNewInstance(clazz, typedValue, timestamp)
	}
	
	LongDataHolder copy(){
		def target = new LongDataHolder()
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
	
}
