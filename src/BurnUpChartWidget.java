/*
 * Copyright (C) 2004-2015 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2015 Polarion Software
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
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetRenderingContext;
/* @wi drivepilot/DP-529	*/
public class BurnUpChartWidget extends AbstractBurnChartWidget {

    public static final String ID = "com.polarion.burnUpChart"; //$NON-NLS-1$

    @Override
    @NotNull
    protected AbstractChartWidgetRenderer createRenderer(@NotNull RichPageWidgetRenderingContext context) {
        return new BurnUpChartWidgetRenderer(context);
    }

    @Override
    @NotNull
    public String getIcon(@NotNull RichPageWidgetContext widgetContext) {
        return "/polarion/ria/images/widgets/burnup.png"; //$NON-NLS-1$
    }

    @Override
    @NotNull
    public String getLabel(@NotNull SharedContext context) {
        return context.localization().getString("richpages.widget.burnUpChart.label"); //$NON-NLS-1$
    }

    @Override
    @NotNull
    public String getDetailsHtml(@NotNull RichPageWidgetContext widgetContext) {
        return widgetContext.localization().getString("richpages.widget.burnUpChart.detailsHtml"); //$NON-NLS-1$
    }
}
