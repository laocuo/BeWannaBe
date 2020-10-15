# -*- coding: utf-8 -*-

from tkinter import *
import tkinter.filedialog as filedialog
import tkinter.messagebox as messagebox
import os
from convertor_icon import *
import base64
import xlrd
import xlwt

root = Tk()
appicon = open("app.ico", "wb+")
appicon.write(base64.b64decode(icon_img))
appicon.close()
root.title('慧婷文件转换工具-0928')
root.iconbitmap("app.ico")
root.resizable(0,0)
os.remove("app.ico")

class Application(Frame):

    RESULT = []

    def __init__(self,):
        super(Application, self).__init__()
        self.createwidgets()

    def createwidgets(self):
        panel = LabelFrame(root)
        panel.pack(anchor=W, fill=X)
        self.path = StringVar()
        Label(panel, textvariable=self.path, width=80).grid(row=0, column=0, columnspan=4)
        self.path.set('表单文件(excel)')
        Button(panel, text='导入', width=20, command=self.daoru).grid(row=1, column=0, columnspan=1)
        Button(panel, text='转换', width=20, command=self.convert).grid(row=1, column=1, columnspan=1)
        Button(panel, text='导出', width=20, command=self.daochu).grid(row=1, column=2, columnspan=1)

    def daoru(self):
        default_dir = r"文件路径"
        file_path  = filedialog.askopenfilename(
            title=u'选择文件',
            initialdir=(os.path.expanduser(default_dir)),
            filetypes=[("表单文件",".xls;*.xlsx")])
        if len(file_path) > 0:
            self.path.set(file_path)

    def convert(self):
        if self.path.get() == '表单文件(excel)':
            messagebox.showerror('Error', '请选择excel文件')
        else:
            self.RESULT = []
            print(self.path.get())
            book = xlrd.open_workbook(self.path.get())

            table = book.sheet_by_index(0)
            firstrow = table.row_values(0)
            firstitem = []
            index = 0
            for i in range(len(firstrow)):
                firstitem.append(firstrow[i])
                if firstrow[i] == '修改金额':
                    index = i
            self.RESULT.append(firstitem)

            for x in range(1, table.nrows):
                itemrow = table.row_values(x)
                print(itemrow)
                money = float(itemrow[index])
                money = round(money, 2)
                while money < -500:
                    item = []
                    for i in range(len(itemrow)):
                        if i == index:
                            item.append(-500)
                        else:
                            item.append(itemrow[i])
                    self.RESULT.append(item)
                    money += 500
                item = []
                for i in range(len(itemrow)):
                    if i == index:
                        item.append(money)
                    else:
                        item.append(itemrow[i])
                self.RESULT.append(item)
            messagebox.showinfo('Info', '数据已经转换')
            print(self.RESULT)

    def daochu(self):
        if self.path.get() == '表单文件(excel)':
            messagebox.showerror('Error', '请选择excel文件')
        else:
            default_dir = r"文件路径"
            file_path  = filedialog.asksaveasfilename(
                title=u'导出文件',
                initialdir=(os.path.expanduser(default_dir)),
                filetypes=[("excel文件",".xls;*.xlsx")])
            if len(file_path) > 5:
                if not (file_path.endswith('.xls') or file_path.endswith('.xlsx')):
                    file_path += '.xls'
                outWorkBook = xlwt.Workbook()
                outWorkSheet = outWorkBook.add_sheet('sheet1')
                for i in range(len(self.RESULT)):
                    item = self.RESULT[i]
                    for j in range(len(item)):
                        outWorkSheet.write(i, j, item[j])
                outWorkBook.save(file_path)
                messagebox.showinfo('Info', '导出成功')

app = Application()
app.mainloop()
