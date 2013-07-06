using Common;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WebService.JsonConverters
{
    public class JsonTaskTypeConverter : JsonConverter
    {
        public override bool CanConvert(Type objectType)
        {
            return objectType == typeof(TaskType);
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            switch ((string)reader.Value)
            {
                case "GPS":
                    return TaskType.GPS;
                case "ABC":
                    return TaskType.ABCD;
            }

            throw new InvalidOperationException("JsonConverter: TaskType unrecognized.");
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            switch ((TaskType)value)
            {
                case TaskType.GPS:
                    writer.WriteValue("GPS");
                    break;
                case TaskType.ABCD:
                    writer.WriteValue("ABC");
                    break;
            }   
        }
    }
}
