/*
 * Copyright (C) 2004-2013 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2013 Polarion Software
 * All Rights Reserved.  No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software.  This notice must be
 * included on all copies, modifications and derivatives of this
 * work.
 *
 * POLARION SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES 
 * ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. POLARION SOFTWARE
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.polarion.alm.server.api.model.rp.widget;

import org.jetbrains.annotations.NotNull;

import com.polarion.alm.shared.api.SharedContext;
import com.polarion.alm.shared.api.model.rp.parameter.DataSetParameter;
import com.polarion.alm.shared.api.model.rp.parameter.ParameterFactory;
import com.polarion.alm.shared.api.model.rp.parameter.RichPageParameter;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetRenderingContext;
import com.polarion.alm.shared.api.utils.SharedLocalization;
import com.polarion.alm.shared.api.utils.collections.ReadOnlyStrictMap;
import com.polarion.alm.shared.api.utils.collections.StrictMap;
import com.polarion.alm.shared.api.utils.collections.StrictMapImpl;

/*
*	@wi.implements SPOC-1719 updated the class
*/
public class MultiDataSetPieChartWidget extends AbstractPieChartWidgetImpl {
    public static final String ID = "com.polarion.multiSetPieChart"; //$NON-NLS-1$

    static final String sectors = "sectors"; //$NON-NLS-1$
    static final String sectorName = "sectorName"; //$NON-NLS-1$
    static final String sectorColor = "sectorColor"; //$NON-NLS-1$

    @Override
    @NotNull
    public String getIcon(@NotNull RichPageWidgetContext widgetContext) {
        return "/polarion/ria/images/widgets/pie_green.png"; //$NON-NLS-1$
    }

    @Override
    @NotNull
    public String getLabel(@NotNull SharedContext context) {
        return context.localization().getString("richpages.widget.multiDataSetPieChart.label"); //$NON-NLS-1$
    }

    @Override
    @NotNull
    public String getDetailsHtml(@NotNull RichPageWidgetContext widgetContext) {
        return widgetContext.localization().getString("richpages.widget.multiDataSetPieChart.detailsHtml"); //$NON-NLS-1$
    }

    @Override
    @NotNull
    public ReadOnlyStrictMap<String, RichPageParameter> getParametersDefinition(@NotNull ParameterFactory factory) {
        SharedLocalization localization = factory.context().localization();

        final String sectorsLabel = localization.getString("richpages.widget.multiDataSetPieChart.parameters.sectors.title"); //$NON-NLS-1$
        final String sectorLabel = localization.getString("richpages.widget.multiDataSetPieChart.parameters.sector.title"); //$NON-NLS-1$
        final String sectorNameLabel = localization.getString("richpages.widget.multiDataSetPieChart.parameters.sectorName.title"); //$NON-NLS-1$
        final String sectorColorLabel = localization.getString("richpages.widget.multiDataSetPieChart.parameters.sectorColor.title"); //$NON-NLS-1$

        StrictMap<String, RichPageParameter> parameters = new StrictMapImpl<String, RichPageParameter>();
        defineTitleParameter(factory, parameters);

        DataSetParameter sectorParameter = factory.dataSet(sectorLabel)
                .add(sectorName, factory.string(sectorNameLabel).build())
                .add(sectorColor, factory.string(sectorColorLabel).build())
                .build();
        parameters.put(sectors, factory.multi(sectorsLabel, sectorParameter).build());

        defineShowDataLabelsParameter(factory, parameters);
        defineShowZeroValuesParameter(factory, parameters);

        return parameters;
    }

    @Override
    @NotNull
    protected AbstractChartWidgetRenderer createRenderer(@NotNull RichPageWidgetRenderingContext context) {
        return new MultiDataSetPieChartWidgetRenderer(context);
    }
}
