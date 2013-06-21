using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WebService.JsonConverters
{
    public class JsonDateTimeConverter : JsonConverter
    {
        public override bool CanConvert(Type objectType)
        {
            return objectType == typeof(DateTime);
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            return JavaTimeStampToDateTime((long)reader.Value);
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            writer.WriteValue(DateTimeToJavaTimeStamp((DateTime)value));
        }

        public DateTime JavaTimeStampToDateTime(long javaTimeStamp)
        {
            DateTime dtDateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0);
            dtDateTime = dtDateTime.AddSeconds(javaTimeStamp / 1000).ToLocalTime();
            return dtDateTime;
        }

        public long DateTimeToJavaTimeStamp(DateTime date)
        {
            return (long)(date - new DateTime(1970, 1, 1, 0, 0, 0)).TotalSeconds * 1000;
        }
    }
}
