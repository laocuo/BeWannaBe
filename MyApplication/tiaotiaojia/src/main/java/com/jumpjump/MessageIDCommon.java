/*
* Copyright (C) 2013-2016 laocuo@163.com .
* Modification based on code covered by the mentioned copyright
* and/or permission notice(s).
*/
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jumpjump;

public class MessageIDCommon {
    public static int INIT = 0x01;

    //MainActivity Msg
    public static int CLIENT_RETURN_ROOM_NAME = 0x101;
    public static int CLIENT_SEARCH_ROOM_DONE = 0x102;
    public static int CLIENT_JOIN_ROOM_SUCCESS = 0x103;
    public static int CLIENT_RETURN_SERVER_STEP = 0x104;
    public static int CLIENT_RETURN_SERVER_RESET = 0x105;
    public static int CLIENT_RETURN_SERVER_CONFIRM_RESET = 0x106;
    public static int CLIENT_JOIN_ROOM_FAIL = 0x107;

    public static int SERVER_RETURN_JOIN_ROOM_SUCCESS = 0x201;
    public static int SERVER_RETURN_CLIENT_STEP = 0x202;
    public static int SERVER_RETURN_CLIENT_CONFIRM_RESET = 0x203;
    public static int SERVER_RETURN_CLIENT_RESET = 0x204;

    public static int MAIN_HANDLER_TIMER_30S = 0x205;

    //ClientHandler Msg
    public static int CLIENT_JOIN_ROOM = 0x301;
    public static int CLIENT_NOTIFY_CHESS_STEP = 0x302;
    public static int CLIENT_TIMER_ONE_SECOND = 0x303;

    //ServerHandler Msg
    public static int SERVER_NOTIFY_CHESS_STEP = 0x401;
    public static int SERVER_TIMER_ONE_SECOND = 0x402;

    public static int THREAD_SLEEP_TIME = 1000;
    public static int TOAST_TIME = 1000;
    public static int SERVER_PORT = 8584;
    public static int SERVER_TCP_PORT = 8585;
    public static int SERVER_UDP_PORT = 8586;
    public static String SERVER_BROAD_ADDRESS = "255.255.255.255";
}
