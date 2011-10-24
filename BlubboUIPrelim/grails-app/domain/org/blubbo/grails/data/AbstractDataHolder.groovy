package org.blubbo.grails.data

import java.util.Map;

import org.blubbo.data.IAgedHolder
import org.blubbo.data.IDataTag
import org.scadajack.grails.util.DomainHelperService

class AbstractDataHolder {

    static constraints = {
    }
	
	static belongsTo = DataTag //[dataTag : DataTag]
	static mapping = {
		tablePerHierarchy false
		version false
	}
	static transients = ['sjDataHolderStale','valueClass','valueString']
	
	static List knownDataHolderClasses = ['Boolean','Byte','Short','Integer','Long','Float','Double','String']
	
	static List knownDataHolderClassesClasses = [java.lang.Boolean,
												 java.lang.Byte,
												 java.lang.Short,
												 java.lang.Integer,
												 java.lang.Long,
												 java.lang.Float,
												 java.lang.Double,
												 java.lang.String]
	
	static AbstractDataHolder getDataHolderForClass(Class clazz){
		getDataHolderForClass(clazz.getName())
	}
	
	static AbstractDataHolder getDataHolderForClass(String className){
		def result;
		switch (className){
			case ['Boolean','java.lang.Boolean']: result = new BooleanDataHolder(); 
												  break;
			case ['Byte','java.lang.Byte']:
			case ['Short','java.lang.Short']:
			case ['Integer','java.lang.Integer']:
			case ['Long','java.lang.Long']: result =  new LongDataHolder(); break;
			case ['Float','java.lang.Float']:
			case ['Double','java.lang.Double']: result =  new DoubleDataHolder(); break;
			case ['String','java.lang.String'] : result =  new StringDataHolder(); break;
			default : result = null
		}
		if (result){
			result.valueClassName = className
		}
		return result;
	}
	
	static AbstractDataHolder holderFromCSVMap(Map map){
		String className = map['CLASS']
	
		switch (className){
			case ['Boolean','java.lang.Boolean']: return BooleanDataHolder.subHolderFromCSVMap(map); break;
			case ['Byte','java.lang.Byte']:
			case ['Short','java.lang.Short']:
			case ['Integer','java.lang.Integer']:
			case ['Long','java.lang.Long']: return LongDataHolder.subHolderFromCSVMap(map); break;
			case ['Float','java.lang.Float']:
			case ['Double','java.lang.Double']: return DoubleDataHolder.subHolderFromCSVMap(map); break;
			case ['String','java.lang.String'] : return StringDataHolder.subHolderFromCSVMap(map); break;
			default : return null
		}
	}
	
	/**
	 * valueClass holds the actual class of the tag we want in the application.
	 * In the SQL database, we're putting all integer type values in a LongInteger 
	 * table, for example. But this is only for the convenience of the table. Outside 
	 * of the repository, we want to have the actual type specific tag, Byte for example. 
	 * So this entry specifies the actual value class we want.
	 */
	Class valueClass
	String valueClassName	// This name can be the simple name in most cases.
	Long timestamp
	
	
	boolean sjDataHolderStale;
	
	protected AbstractDataHolder(){
		timestamp = (new Date()).getTime()
	}
	
	protected AbstractDataHolder(IDataTag<?> dataTag){
		timestamp = dataTag.getValueHolder()?.getTimestamp();
		if (timestamp == null){
			timestamp = (new Date()).getTime()
		}
		valueClassName = dataTag.getValueClass()
	}
	
	void setSjDataHolderStale(boolean stale){
		if (this.sjDataHolderStale != stale){
			this.sjDataHolderStale = stale
			//log.info "sjDataHolderStale set to ${stale}"
		}
	}
	
	void setSjDataHolderStale(String source, boolean stale){
		//log.info "sjDataHolderStale called by ${source} with stale = ${stale}"
		setSjDataHolderStale(stale)
	}
	
	protected boolean checkSjDataTagCurrent(IDataTag<?> sjTag){
		def dataHolder = sjTag.getValueHolder()
		if (!(sjTag.valueClass in [valueClass])){ return false }
		if (dataHolder?.getTimestamp() != timestamp){ return false }
		setSjDataHolderStale('checkSjDataTagCurrent',false)
		return true
	}
	
	public Class qualifiedClassFromValueClassName(){
		if (!valueClassName){
			return null
		}
		String className = valueClassName
		if (DomainHelperService.qualifiedClassNameMap[valueClassName]){
			className = DomainHelperService.qualifiedClassNameMap[valueClassName]
		}
		try{
			Class clazz = Class.forName(className)
			return clazz
		} catch (Exception e){
			return null
		}
	}
	
	void setValueClass(Class valueClass){
		if (this.valueClass != valueClass){
			setSjDataHolderStale('setValueClass',true)
			this.valueClass = valueClass;
		}
		this.valueClassName = valueClass.simpleName
	}
	
	void setValueClassName(String valueClassName){
		if (this.valueClassName != valueClassName){
			this.valueClassName = valueClassName
		}
		Class qClass = qualifiedClassFromValueClassName()
		valueClass = qClass
		
	}
	
	void setTimestamp(Long timestamp){
		if (this.timestamp != timestamp){
			setSjDataHolderStale('setTimestamp',true)
			this.timestamp = timestamp;
		}
	}
	
	
	protected void updateFromDataHolder(IAgedHolder<?> dataHolder){
		this.timestamp = dataHolder.timestamp
		this.valueClass = dataHolder.valueClass
		setSjDataHolderStale('updateFromDataHolder',false)
	}
	
	/**
	 * Think we need to have this here so that we don't have to make AbstractDataHolder actually abstract.
	 * And we want to call it anyway so we reset sjDataHolderStale
	 * @return
	 */
	protected IAgedHolder<?> newDataHolder(){
		setSjDataHolderStale('newDataHolder',false)
		return null	
	}
	
	AbstractDataHolder copy(){
		
	}
	
	protected AbstractDataHolder copyFields(AbstractDataHolder target){
		target.properties = this.properties
		return target
	}
	
	String getValueString(){
		''
	}
}
