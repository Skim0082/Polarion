package com.polarion.widget.linkroleCockpit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.gwt.core.shared.GwtIncompatible;
import com.polarion.alm.shared.api.SharedContext;
import com.polarion.alm.shared.api.model.document.Document;
import com.polarion.alm.shared.api.model.parameter.Parameter;
import com.polarion.alm.shared.api.model.rp.parameter.BooleanParameter;
import com.polarion.alm.shared.api.model.rp.parameter.EnumParameter;
import com.polarion.alm.shared.api.model.rp.parameter.ObjectSelectorParameter;
import com.polarion.alm.shared.api.model.rp.parameter.ParameterFactory;
import com.polarion.alm.shared.api.model.rp.parameter.RichPageParameter;
import com.polarion.alm.shared.api.model.rp.parameter.ScopeParameter;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidget;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetActionContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetCommonContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetDependenciesContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetRenderingContext;
import com.polarion.alm.shared.api.utils.collections.ReadOnlyStrictMap;
import com.polarion.alm.shared.api.utils.collections.StrictMap;
import com.polarion.alm.shared.api.utils.collections.StrictMapImpl;
import com.polarion.alm.tracker.ITrackerService;
import com.polarion.alm.tracker.model.IWorkItem;
import com.polarion.platform.core.PlatformContext;
import com.polarion.platform.persistence.model.IPObjectList;
/*
* @wi.implements SPOC-1555 add java aaa
*/
public class LinkRoleCockpit extends RichPageWidget {
	
	// Meta Information for Widget
	private static final String LINKROLE_COCKPIT_WIDGET_ICON = "IconPurgeLinkCockpit.png";	
	private static final String WIDGET_LABEL = "PurgeLinkRole Cockpit";
	private static final String WIDGET_DETAILS = "PurgeLinkRole Cockpit provides a solution to delete links between documents";
	
	//Parameter Labels
	static final String PARAMETER_PROJECT = "Target Project";
    static final String PARAMETER_SOURCE_DOCUMENT = "Source Document";
    static final String PARAMETER_BETWEEN_DOCUMENTS = "Between Documents";
    static final String PARAMETER_LINKROLE = "Linkrole";
    static final String PARAMETER_TARGET_DOCUMENT = "Target Document";
    
    //Texts for UI
    static final String TEXT_TABLE_SOURCEDOCUMENT_NOT_SELECTED = "Please select a source Document first!";
    static final String TEXT_TABLE_LINKROLE_NOT_SELECTED = "Please select a linkrole which should be deleted";
    static final String TEXT_TABLE_TARGETDOCUMENT_NOT_SELECTED = "Please select a target document";
    static final String TEXT_TABLE_TARGETDOCUMENT_NOT_USED = "it will be not used, because of Between Documents value";
    static final String TEXT_TABLE_IN_SPACE = " in Space: ";
    static final String TEXT_TABLE_IN_PROJECT = " in Project: ";
    
    static final String TEXT_LOG_HEADER_INCLUDING_TABLE = "The following Items were updated: <table border='1'> <tr><th>Item</th><th>Linked Item</th></tr>";
    static final String TEXT_NO_SOURCE_DOCUMENT_SELECTED = "Please select a source Document!";
    static final String TEXT_NO_LINKROLE_SELECTED = "Please select a Linkrole!";
    static final String TEXT_NO_TARGET_DOCUMENT_SELECTED = "Please select the target document or change the between Documents value!";
    
    static final String TEXT_BUTTON_BACK = "<button type='button' id='back'" + RichPageWidget.ATTRIBUTE_ACTION_ID + "='backClick'" + ">Back</button>";
    
    
	@Override
	public String getDetailsHtml(RichPageWidgetContext context) {
		return WIDGET_DETAILS;
	}

	@Override
	public String getIcon(RichPageWidgetContext context) {
		return context.resourceUrl(LINKROLE_COCKPIT_WIDGET_ICON);
	}
	
	@Override
    @NotNull
    @GwtIncompatible
	public InputStream getResourceStream(@NotNull final String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IOException(String.format("Requested resource '%s' was not found.", path));
        }
        return stream;
    }

	@Override
	public String getLabel(SharedContext context) {
		return WIDGET_LABEL;
	}

	@Override
	public ReadOnlyStrictMap<String, RichPageParameter> getParametersDefinition(ParameterFactory factory) {
		List<String> allowedPrototypesList = new ArrayList<String>();
    	allowedPrototypesList.add("Document");
        StrictMap<String, RichPageParameter> parameters = new StrictMapImpl<String, RichPageParameter>();
        parameters.put(PARAMETER_SOURCE_DOCUMENT, factory.objectSelector(PARAMETER_SOURCE_DOCUMENT).allowedPrototypes(allowedPrototypesList).build());
        parameters.put(PARAMETER_LINKROLE, factory.enumeration(PARAMETER_LINKROLE, "workitem-link-role").allowMultipleValues(false).build());
        parameters.put(PARAMETER_BETWEEN_DOCUMENTS, factory.bool(PARAMETER_BETWEEN_DOCUMENTS).build());
        parameters.put(PARAMETER_PROJECT, factory.scope(PARAMETER_PROJECT).dependencySource(true).build());
        parameters.put(PARAMETER_TARGET_DOCUMENT, factory.objectSelector(PARAMETER_TARGET_DOCUMENT).allowedPrototypes(allowedPrototypesList).dependencyTarget(true).addCurrentScopeToDefaultQuery(false).build());

        return parameters;
	}
	
/**		
* @wi.implements drivepilot/DP-529 link to work item DP-529
* Change comment
*/
	public void processParameterDependencies(@NotNull RichPageWidgetDependenciesContext context) {
        ObjectSelectorParameter targetDocumentParameter = context.parameter(PARAMETER_TARGET_DOCUMENT);
        ScopeParameter scopeParameter = context.parameter(PARAMETER_PROJECT); 
        
        targetDocumentParameter.set().defaultQuery(scopeParameter.scope().projectId());
    }

	@Override
	public String renderHtml(RichPageWidgetRenderingContext context) {
		return
		"<table border='1'>" + 
	    "<tr>" + "<td>" + PARAMETER_SOURCE_DOCUMENT + "</td>" + "<td>" + getSourceDocumentParameterText(context) + "</td>" + "</tr>" +
	    "<tr>" + "<td>" + PARAMETER_LINKROLE + "</td>" + "<td>" + getLinkRoleParameterText(context) + "</td>" + "</tr>" +
	    "<tr>" + "<td>" + PARAMETER_BETWEEN_DOCUMENTS + "</td>" + "<td>" + getBetweenDocumentsParameterText(context) + "</td>" + "</tr>" +
	    "<tr>" + "<td>" + PARAMETER_TARGET_DOCUMENT + "</td>" + "<td>" + getTargetDocumentParameterText(context) + "</td>" + "</tr>" +
	    "</table>" + "</br>" +
		"<button type='button' id='execute'" + RichPageWidget.ATTRIBUTE_ACTION_ID + "='executeClick'" + RichPageWidget.ATTRIBUTE_CONFIRM_TITLE + "='Confirmation'" + RichPageWidget.ATTRIBUTE_CONFIRM_TEXT + "='Are you sure you want to delete all the choosen links?'" + ">Execute</button>";
	}
	
	public String getSourceDocumentParameterText(RichPageWidgetRenderingContext context){
		ObjectSelectorParameter sourceDocumentParameter =  context.parameter(PARAMETER_SOURCE_DOCUMENT);
		if (sourceDocumentParameter.value() == null) {
			return TEXT_TABLE_SOURCEDOCUMENT_NOT_SELECTED;
		}
		else{
			return sourceDocumentParameter.value().label() + TEXT_TABLE_IN_SPACE + sourceDocumentParameter.value().getOldApi().getLocalId().getContainerId().getObjectName();
		}
	}
	
	public String getLinkRoleParameterText(RichPageWidgetRenderingContext context){
		EnumParameter linkroleParameter = context.parameter(PARAMETER_LINKROLE);
		if (linkroleParameter.singleValue() == null) {
			return TEXT_TABLE_LINKROLE_NOT_SELECTED;
		}
		else{
			return linkroleParameter.singleValue().label();
		}	
	}
	
	public String getBetweenDocumentsParameterText(RichPageWidgetRenderingContext context){
		BooleanParameter betweenDocumentsParameter = context.parameter(PARAMETER_BETWEEN_DOCUMENTS);
		return String.valueOf(betweenDocumentsParameter.value());
	}
	
	public String getTargetDocumentParameterText(RichPageWidgetRenderingContext context){
		BooleanParameter betweenDocumentsParameter = context.parameter(PARAMETER_BETWEEN_DOCUMENTS);
		ObjectSelectorParameter targetDocumentParameter = context.parameter(PARAMETER_TARGET_DOCUMENT);
		if (targetDocumentParameter.value() == null && betweenDocumentsParameter.value() == true) {
			return TEXT_TABLE_TARGETDOCUMENT_NOT_SELECTED;
		}
		else if (betweenDocumentsParameter.value() == false) {
			return TEXT_TABLE_TARGETDOCUMENT_NOT_USED;
		}
		else{
			return targetDocumentParameter.value().label() + TEXT_TABLE_IN_SPACE + targetDocumentParameter.value().getOldApi().getLocalId().getContainerId().getObjectName() + TEXT_TABLE_IN_PROJECT + targetDocumentParameter.value().getReference().projectId();
		}
	}
	

	
    @SuppressWarnings("unchecked")
	@Override
    public void executeAction(@NotNull RichPageWidgetActionContext context) {
    	ObjectSelectorParameter sourceDocumentParameter =  context.parameter(PARAMETER_SOURCE_DOCUMENT);
    	EnumParameter linkroleParameter = context.parameter(PARAMETER_LINKROLE);
    	BooleanParameter betweenDocumentsParameter = context.parameter(PARAMETER_BETWEEN_DOCUMENTS);
    	ObjectSelectorParameter targetDocumentParameter = context.parameter(PARAMETER_TARGET_DOCUMENT);
    	ITrackerService trackerService = PlatformContext.getPlatform().lookupService(ITrackerService.class);
    	String htmlStringUpdated = TEXT_LOG_HEADER_INCLUDING_TABLE;
    	
    	if (context.actionId().equals("executeClick")) {
    		//Check for first Parameter Source Document
    		if (sourceDocumentParameter.value() == null) {
				context.replaceWithHtml(TEXT_NO_SOURCE_DOCUMENT_SELECTED + "</br>" + TEXT_BUTTON_BACK);
			}
    		else{
    			Document sourceDocument = context.transaction().documents().getBy().projectSpaceAndName(sourceDocumentParameter.value().getReference().projectId(), sourceDocumentParameter.value().getOldApi().getLocalId().getContainerId().getObjectName(), sourceDocumentParameter.value().label());
    			List<IWorkItem> listContainedWorkItems = sourceDocument.getOldApi().getContainedWorkItems();
    			//Check for second Parameter Linkrole
    			if (linkroleParameter.singleValue() == null) {
    				context.replaceWithHtml(TEXT_NO_LINKROLE_SELECTED + "</br>" + TEXT_BUTTON_BACK);
				}
    			else{
    				//Check if between Documents is checked
    				if (betweenDocumentsParameter.value() == true) {
						//Check if target Document is empty
    					if (targetDocumentParameter.value() == null) {
    						context.replaceWithHtml(TEXT_NO_TARGET_DOCUMENT_SELECTED + "</br>" + TEXT_BUTTON_BACK);
						}
    					else{
    						for (IWorkItem containedItem : listContainedWorkItems) {
								if (!containedItem.getType().getId().equals("heading")) {
									
									IPObjectList<IWorkItem> linkedItems = containedItem.getLinkedWorkItems();
									
									for (IWorkItem linkItem : linkedItems) {
										if (linkItem.getModule() != null) {										
													
											if (linkItem.getModule().getProjectId().equals(targetDocumentParameter.value().getReference().projectId()) && linkItem.getModule().getModuleFolder().equals(targetDocumentParameter.value().getOldApi().getLocalId().getContainerId().getObjectName()) && linkItem.getModule().getModuleName().equals(targetDocumentParameter.value().label())) {
												Boolean updated = containedItem.removeLinkedItem(linkItem, trackerService.getTrackerProject(context.getDisplayedScope().projectId()).getWorkItemLinkRoleEnum().wrapOption(linkroleParameter.singleValue().id()));
												containedItem.save();
												if (updated == true) {
													htmlStringUpdated += "<tr>" + "<td>" + containedItem.getId() + "</td>" + "<td>" + linkItem.getId() + "</td>" + "</tr>";
												}
											}
										}
									}
								}
							}
    						htmlStringUpdated += "</table>";
        					context.replaceWithHtml(htmlStringUpdated);
    					}
					}
    				else{
    					for (IWorkItem containedItem : listContainedWorkItems) {
							if (!containedItem.getType().getId().equals("heading")) {
								
								IPObjectList<IWorkItem> linkedItems = containedItem.getLinkedWorkItems();
								
								for (IWorkItem linkItem : linkedItems) {
									Boolean updated = containedItem.removeLinkedItem(linkItem, trackerService.getTrackerProject(context.getDisplayedScope().projectId()).getWorkItemLinkRoleEnum().wrapOption(linkroleParameter.singleValue().id()));
									containedItem.save();
									
									if (updated == true) {
										htmlStringUpdated += "<tr>" + "<td>" + containedItem.getId() + "</td>" + "<td>" + linkItem.getId() + "</td>" + "</tr>";
									}
								}
							}
						}
    					htmlStringUpdated += "</table>";
    					context.replaceWithHtml(htmlStringUpdated);
    				}
    			}
    		}
    		
			
		}
    	
    	if (context.actionId().equals("backClick")) {
    		context.refresh(true);
		}
        

    }

}
