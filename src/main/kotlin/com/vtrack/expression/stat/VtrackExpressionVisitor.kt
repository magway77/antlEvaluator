package com.vtrack.expression.stat

import com.vtrack.expression.ExprVisitorBase
import com.vtrack.expression.VtrackExpressionStatParams
import com.vtrack.generated.antlr.VtrackExprBaseVisitor
import com.vtrack.generated.antlr.VtrackExprParser

class VtrackExpressionVisitor :
    VtrackExprBaseVisitor<VtrackExpressionStatParams>(),
    ExprVisitorBase<VtrackExpressionStatParams> {

    override val variables: HashMap<String, VtrackExpressionStatParams> = HashMap()

    override fun defaultResult() = VtrackExpressionStatParams()

    override fun visitExpression(ctx: VtrackExprParser.ExpressionContext?): VtrackExpressionStatParams {
        return VtrackExpressionStatParams().apply {
            ctx!!.let { ctx ->
                this.alertName = ctx.alertName.text
                this.sourceId = ctx.sourceId.text
                this.qualifierName = ctx.qualifierName.text
                this.qualifierValue = ctx.qualifierValue.qualifierRangeValue.text
                this.rangeLowValue = ctx.thresholdRange.lowValue?.text?.toIntOrNull()
                this.rangeHighValue = ctx.thresholdRange.highValue?.text?.toIntOrNull()
                val timeRange: VtrackExprParser.TimePeriodContext = ctx.timeRange
                this.durationValue = timeRange.periodValue.text.toIntOrNull()
                this.durationKind = VtrackExpressionStatParams.TimeDurationKind.byValue(timeRange.periodKind.text)
            }
        }
    }

    override fun aggregateResult(
        aggregate: VtrackExpressionStatParams?,
        nextResult: VtrackExpressionStatParams?
    ): VtrackExpressionStatParams {
        return super.aggregateResult(aggregate, nextResult)
    }
}
