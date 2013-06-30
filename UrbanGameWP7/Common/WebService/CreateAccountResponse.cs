using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Common
{
    public enum CreateAccountResponse
    {
        Success,
        LoginUnavailable,
        Timeout,
        UnknownError
    }
}
