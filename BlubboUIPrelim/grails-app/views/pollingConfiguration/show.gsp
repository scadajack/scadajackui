
<%@ page import="org.scadajack.grails.comm.PollingConfiguration" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'pollingConfiguration.label', default: 'PollingConfiguration')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body> 
        <div class="body" style="width:80%">
        	
        	<h1 style="text-align:center"> ${message(code: 'pollConfigSetup.label', default: 'PollingConfigurationSetup')}</h1>
            <g:render template="showPollHorizTempl"/>
            <h1 style="text-align:center">${message(code: 'pollConfigTarget.label', default: 'Target Tags')}</h1>
            <g:render template="/remotePollTarget/pollTargetList" />
            
        </div>
        
        <div style = 'clear:both'>
            
        </div>
    </body>
</html>
