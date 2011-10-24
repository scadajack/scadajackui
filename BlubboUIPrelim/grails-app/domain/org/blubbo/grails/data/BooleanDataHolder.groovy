package org.blubbo.grails.data

import java.util.Map

import org.blubbo.camel.utils.ConvertType
import org.blubbo.data.IAgedHolder
import org.blubbo.data.IDataTag
import org.blubbo.data.impl.DefaultDataHolder

class BooleanDataHolder extends AbstractDataHolder{

    static belongsTo = DataTag
    static constraints = {
    }
	
	static BooleanDataHolder subHolderFromCSVMap(Map map){
		BooleanDataHolder dataHolder = new BooleanDataHolder()
		String dataClass = map['CLASS']
		dataHolder.valueClassName = dataClass
		dataHolder.value = map['VALUE'] as Boolean
		dataHolder.timestamp = map['TIMESTAMP'] ? map['TIMESTAMP'] as Long : (new Date()).getTime()

		return dataHolder
    }
	
	
	Boolean value
	Boolean defaultValue = false;
	
	BooleanDataHolder(){}
	
	BooleanDataHolder(IDataTag<Boolean> dataTag){
		super(dataTag)
	}
	
	void setValue(Boolean value){
		if (this.value != value){
			setSjDataHolderStale(true)
			this.value = value
		}
	}
	
	void updateFromDataHolder(IAgedHolder<?> dataHolder){
		super.updateFromDataHolder(dataHolder)
		this.value = dataHolder.value
	}
	
	public boolean checkSjDataTagCurrent(IDataTag<?> sjTag){
		if (value != sjTag.getValue()) { return false }
		if (!super.checkSjDataTagCurrent(sjTag)) { return false }
		return true
	}
	
	protected DefaultDataHolder<?> newDataHolder(){
		super.newDataHolder()
		Class<?> clazz = valueClass;
		def typedValue = ConvertType.convertTo(clazz,value)
		return DefaultDataHolder.getNewInstance(clazz, typedValue, timestamp)
	}
	
	BooleanDataHolder copy(){
		def target = new BooleanDataHolder()
		super.copyFields(target)
		target.value = value
		return target
	}
	
	String getValueString(){
		value.toString()
	}
	
	String toString(){
		return "$value ($valueClass)"
	}
}
