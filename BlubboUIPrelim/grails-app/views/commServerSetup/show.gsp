
<%@ page import="org.scadajack.grails.comm.CommServerSetup" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'commServerSetup.label', default: 'CommServerSetup')}"/>
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body> 
        <div class="body" style="width:80%">
        	
        	<h1 style="text-align:center"> ${message(code: 'commServerSetup.label', default: 'CommServerSetup')}</h1>
            <g:render template="showHorizTempl"/>
            <h1 style="text-align:center">${message(code: 'serverTarget.label', default: 'Target Tags')}</h1>
            <g:render template="/serverTarget/serverTargetList" />
            
        </div>
        
        <div style = 'clear:both'>
            
        </div>
    </body>
</html>
