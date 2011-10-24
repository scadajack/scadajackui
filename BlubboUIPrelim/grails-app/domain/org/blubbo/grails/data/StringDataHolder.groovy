package org.blubbo.grails.data

import java.util.Map

import org.blubbo.camel.utils.ConvertType
import org.blubbo.data.IAgedHolder
import org.blubbo.data.IDataTag
import org.blubbo.data.constants.IPropertiesConstants
import org.blubbo.data.impl.DefaultDataHolder

class StringDataHolder extends AbstractDataHolder{
	static belongsTo = DataTag
    static constraints = {
		value validator: {val,obj -> 
			if (!(obj?.maxSize)){
				return true;
			}
			obj?.maxSize >= val?.length()
		}
		maxSize(nullable:true)
    }
	
	static StringDataHolder subHolderFromCSVMap(Map map){
		StringDataHolder dataHolder = new StringDataHolder()
		String dataClass = map['CLASS']
		dataHolder.valueClassName = dataClass
		dataHolder.value = map['VALUE']
		dataHolder.timestamp = map['TIMESTAMP'] ? map['TIMESTAMP'] as Long : (new Date()).getTime()
		def dataHolderProps = dataHolder.properties.keySet()
		if ('maxSize' in dataHolderProps && map['MAX_SIZE']){
			dataHolder.maxSize = map['MAX_SIZE'] as Long
		}
		if ('defaultValue' in dataHolderProps && map['DEFAULT_VALUE']){
			dataHolder.defaultValue = map['DEFAULT_VALUE'] as Long
		}
		return dataHolder
	}
	
	//StringConstraint constraint
	
	String value
	Integer maxSize
	String defaultValue = ''
	
	StringDataHolder(){}
	
	StringDataHolder(IDataTag<?> dataTag){
		super(dataTag)
		
		if (valueClass != String.class.name){
			throw new IllegalArgumentException("Invalid DataTag Type applied to StringDataHolder Constructor");
		}
		
		maxSize = dataTag.getProperty(IPropertiesConstants.MAX_SIZE_KEY, Integer.class);
		
		value = dataTag.getValue()?.toString();
	}
	
	void setValue(String value){
		if (this.value != value){
			setSjDataHolderStale(true)
			this.value = value
		}
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
	
	StringDataHolder copy(){
		def target = new StringDataHolder()
		super.copyFields(target)
		target.value = value
		target.maxSize = maxSize
		return target
	}
	
	String getValueString(){
		value.toString()
	}
	
	String toString(){
		return "$value ($valueClass)"
	}
}
