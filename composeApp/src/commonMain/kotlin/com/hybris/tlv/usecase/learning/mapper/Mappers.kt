package com.hybris.tlv.usecase.learning.mapper

import com.hybris.tlv.database.LearningSchema
import com.hybris.tlv.usecase.learning.model.Learning

internal fun Learning.toLearningSchema(): LearningSchema =
    LearningSchema(
        id = id,
        description = description,
        image = image,
        type = type,
    )

internal fun LearningSchema.toLearning(): Learning =
    Learning(
        id = id,
        description = description,
        image = image,
        type = type,
    )
