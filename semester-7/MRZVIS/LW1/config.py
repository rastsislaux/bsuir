from lib.normalization import ImageVectorConverter
from lib.serialization import LzmaSerializationStrategy

SERIALIZATION_STRATEGY = LzmaSerializationStrategy()
NORMALIZER = ImageVectorConverter()
